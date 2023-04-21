(ns youtube.db
  (:require [youtube.config :as config]
            [yesql.core :refer [defqueries]]
            [cheshire.core :as json]))

(def db-spec {:connection-uri config/database-url})

(defqueries config/query-file
  {:connection db-spec})

(defn eprintln [& more]
  (binding [*out* *err*]
    (apply println more)))

(defn insert-all-videos [videos]
  (doseq [video videos]
    (try
      (insert-video! {:videoId (get-in video [:id :videoId])
                      :publishedAt (get-in video [:snippet :publishedAt])
                      :channelId (get-in video [:snippet :channelId])
                      :video (json/generate-string video {:pretty true})})
      (catch Exception e (eprintln (.getMessage e))))))

(defn select-all-videos []
  (->> (select-videos {:channelId config/channel-id})
       (map :video)
       (map #(json/parse-string %1 true))))

(defn load-all-videos []
  (let [json-map (json/parse-string (slurp config/json-file) true)]
    (insert-all-videos json-map)))
