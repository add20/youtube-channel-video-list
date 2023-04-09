(ns youtube.core
  (:require [youtube.config :as config]
            [youtube.fetch :as fetch]
            [youtube.view :as view]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-f" "--fetch" "fetch YouTube data"]
   ["-g" "--generate-html" "generate html file"]
   ["-h" "--help"]])

(def options (atom {}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (reset! options (parse-opts args cli-options))

  (when (get-in @options [:options :help])
    (println "usage:")
    (println (:summary @options))
    (System/exit 0))

  (.mkdirs (io/file config/work-dir))
  (.mkdirs (io/file config/log-dir))

  (when (get-in @options [:options :fetch])
    (println "fetch YouTube data.")
    (fetch/load-videos))

  (when (get-in @options [:options :generate-html])
    (println "generate YouTube channel video HTML.")
    (view/generate-html)))
