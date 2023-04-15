(ns youtube.fetch
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [youtube.config :as config]
            [youtube.date :as date]))

(defn log [content file-path]
  (letfn [(escape [str] (str/replace str #"[:-]" "."))]
    (spit (escape (str config/log-dir "/" file-path)) content)
    content))

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
        (log (str "search?"
                  (query-string (select-keys params [:publishedAfter :publishedBefore :pageToken]))
                  ".json"))
        (json/parse-string true))))

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

(defn get-all-channel-videos [api-key channel-id]
  (->> date/periods
       (map (fn [[start end]]
              (get-channel-videos api-key channel-id start end)))
       (apply concat)))

(def channel-videos
  (delay (get-all-channel-videos config/api-key config/channel-id)))

(defn load-videos []
  (let [json-str (json/generate-string @channel-videos {:pretty true})]
    (printf "video count is %d.\n" (count @channel-videos))
    (spit config/json-file json-str)))
