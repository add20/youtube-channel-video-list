(ns youtube.core
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.tools.cli :refer [parse-opts]]
            [youtube.config :as config]
            [youtube.db :as db]
            [youtube.fetch :as fetch]
            [youtube.view :as view])
  (:gen-class))

(def cli-options
  [["-f" "--fetch" "fetch YouTube data to JSON"]
   ["-l" "--load" "load YouTube JSON data to SQLite database"]
   ["-g" "--generate-html" "generate HTML file"]
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
  (.mkdirs (io/file config/html-dir))
  (when-not (.exists (io/file config/sqlite-file))
    (sh/sh "bash" "-c" (str "cat resources/table.sql | sqlite3 " config/sqlite-file)))

  (when (get-in @options [:options :fetch])
    (println "fetch YouTube data.")
    (fetch/load-videos))

  (when (get-in @options [:options :load])
    (println "load YouTube data into sqlite database.")
    (db/load-all-videos))

  (when (get-in @options [:options :generate-html])
    (println "generate YouTube channel video HTML.")
    (view/generate-html)))
