(ns youtube.fetch
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.string :as str]
            [youtube.config :as config]
            [youtube.date :as date]))

(defn log-http-body [content file-path]
  (letfn [(escape [str] (str/replace str #"[:-]" "."))]
    (spit (escape (str config/log-dir "/" file-path)) content)
    content))

(defn query-string [params]
  (->> params
       seq
       (map (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))

(defn fetch-channel-video-ids [api-key channel-id page-token start end]
  (let [base-url "https://www.googleapis.com/youtube/v3/search"
        params {:part "id"
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
        (json/parse-string true))))

(defn get-all-page-channel-video-ids [api-key channel-id start end]
  (loop [video-ids []
         page-token nil]
    (let [data (fetch-channel-video-ids api-key channel-id page-token start end)
          ids (map #(get-in %1 [:id :videoId]) (:items data))
          next-page-token (:nextPageToken data)
          result-video-ids (concat video-ids ids)]
      (printf "ids count is %d.\n" (count ids))
      (if next-page-token
        (recur result-video-ids next-page-token)
        result-video-ids))))

(defn get-all-period-channel-video-ids [api-key channel-id]
  (->> (date/periods config/register-date config/latest-date config/interval-month)
       (map (fn [[start end]]
              (get-all-page-channel-video-ids api-key channel-id start end)))
       (apply concat)))

(defn fetch-channel-videos [api-key id]
  (let [base-url "https://www.googleapis.com/youtube/v3/videos"
        params {:part "id,snippet,statistics"
                :key api-key
                :id id}]
    (println (str base-url "?" (query-string params)))
    (-> (http/get base-url {:query-params params})
        :body
        ;; don't include log file name 'id', because occur 'File name too long' error.
        (json/parse-string true))))

(defn get-all-channel-videos [api-key channel-id]
  (->> (get-all-period-channel-video-ids api-key channel-id)
       distinct
       (partition-all 50)
       (map #(->> %1
                  (str/join ",")
                  (fetch-channel-videos api-key)
                  :items))
       (apply concat)))

(def channel-videos
  (delay (get-all-channel-videos config/api-key config/channel-id)))

(defn load-videos []
  (let [json-str (json/generate-string @channel-videos {:pretty true})]
    (printf "total video count is %d.\n" (count @channel-videos))
    (spit config/json-file json-str)))
