(ns youtube.config
  (:require [environ.core :refer [env]]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def database-url (env :database-url))

(def query-file (env :query-file))

(def api-key (env :api-key))

(def channel-id (env :channel-id))

(def time-zone-for-offset (Long/parseLong (env :time-zone-for-offset)))

(defn parse-format [format date-str]
  (-> (f/parse (f/formatter format) date-str)
      (t/from-time-zone (t/time-zone-for-offset time-zone-for-offset))))

(def latest-date (let [s (env :latest-date)]
                   (if (= s "now")
                     (t/now)
                     (parse-format "yyyy/MM/dd HH:mm:ss" s))))

(def register-date (parse-format "yyyy/MM/dd" (env :register-date)))

(def interval-month (Long/parseLong (env :interval-month)))

(def work-dir (env :work-dir))

(def log-dir (env :log-dir))

(def json-file (env :json-file))

(def sqlite-file (env :sqlite-file))

(def template-file (env :template-file))

(def html-dir (env :html-dir))