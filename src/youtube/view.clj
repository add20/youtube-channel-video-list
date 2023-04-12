(ns youtube.view
  (:require [clojure.java.io]
            [selmer.parser :as selmer]
            [youtube.config :as config]
            [youtube.db :as db]))

(defn video-url [video-id]
  (str "https://www.youtube.com/watch?v=" video-id))

(selmer/add-filter! :video-url video-url)

(defn generate-html []
  (let [videos (db/select-all-videos)
        template (slurp config/template-file)
        html (selmer/render template {:videos (into [] videos)})]
    (spit config/html-file html)))
