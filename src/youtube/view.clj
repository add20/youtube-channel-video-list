(ns youtube.view
  (:require [youtube.config :as config]
            [clojure.java.io]
            [cheshire.core :as json]
            [selmer.parser :as selmer]))

(defn video-url [video-id]
  (str "https://www.youtube.com/watch?v=" video-id))

(selmer/add-filter! :video-url video-url)

(defn generate-html []
  (let [videos (json/parse-string (slurp config/json-file) true)
        template (slurp config/template-file)
        html (selmer/render template {:videos (into [] videos)})]
    (spit config/html-file html)))
