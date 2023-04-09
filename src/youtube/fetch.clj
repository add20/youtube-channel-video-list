(ns youtube.fetch
  (:require
   [youtube.config :as config]
   [clojure.string :as str]
   [clj-http.client :as http]
   [cheshire.core :as json]
   [clj-time.core :as t]
   [clj-time.format :as f]))

(defn json-read-str [json-str]
  (json/parse-string json-str true))

(defn escape [str]
  (str/replace str #"[:-]" "."))

(defn log-http-body [content file-path]
  (spit (escape (str config/log-dir "/" file-path)) content)
  content)

(defn query-string [params]
  (->> params
       seq
       (map (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))

(defn fetch-youtube-data [api-key channel-id page-token start end]
  (let [base-url "https://www.googleapis.com/youtube/v3/search"
        params {:part "snippet"
                :key api-key
                :channelId channel-id
                :type "video"
                :order "date"
                :maxResults 50
                :publishedAfter start
                :publishedBefore end
                :pageToken page-token}]
    (println (str base-url "?" (query-string params)))
    (-> (http/get base-url {:query-params params})
        :body
        (log-http-body (str "search?"
                            (query-string (select-keys params [:publishedAfter :publishedBefore :pageToken]))
                            ".json"))
        json-read-str)))

(defn get-channel-videos [api-key channel-id start end]
  (loop [videos []
         page-token nil]
    (let [data (fetch-youtube-data api-key channel-id page-token start end)
          items (:items data)
          next-page-token (:nextPageToken data)
          result-videos (concat videos items)]
      (if next-page-token
        (recur result-videos next-page-token)
        result-videos))))

(defn format-date [date]
  (str (f/unparse (f/formatter "yyyy-MM-dd") date)
       "T"
       (f/unparse (f/formatter "HH:mm:ss") date)
       "Z"))

;; (def register-date (t/date-time 2022 6 1 0 0 0))

(def days (let [s (->> (t/now)
                       (iterate #(t/minus %1 (t/months 1)))
                       (take-while #(t/after? %1 config/register-date)))
                v (conj (into [] s) config/register-date)]
            (map format-date v)))

(def periods (map vector (next days) days))

(defn get-all-channel-videos [api-key channel-id]
  (->> periods
       (map (fn [[start end]]
              (get-channel-videos api-key channel-id start end)))
       (apply concat)))

(def channel-videos
  (delay (get-all-channel-videos config/api-key config/channel-id)))

(defn load-videos []
  (let [json-str (json/generate-string @channel-videos {:pretty true})]
    (printf "video count is %d.\n" (count @channel-videos))
    (spit config/json-file json-str)))
