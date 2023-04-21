(ns youtube.view
  (:require [clojure.java.io]
            [selmer.parser :as selmer]
            [youtube.config :as config]
            [youtube.db :as db]))

(defn video-url [video-id]
  (str "https://www.youtube.com/watch?v=" video-id))

(defn comma-number [n-str]
  (when n-str
    (format "%,d" (Integer/parseInt n-str))))

(selmer/add-filter! :video-url video-url)
(selmer/add-filter! :comma-number comma-number)

(defn generate-html []
  (let [videos (db/select-all-videos config/channel-id)
        template (slurp config/template-file)
        html (selmer/render template {:videos (into [] videos)})]
    (spit (str config/html-dir
               "/"
               (get-in (first videos) [:snippet :channelTitle])
               ".html")
          html)))
