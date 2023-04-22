(ns youtube.config
  (:require [environ.core :refer [env]]
            [youtube.date :as date]))

(def database-url (env :database-url))

(def query-file (env :query-file))

(def api-key (env :api-key))

(def channel-id (env :channel-id))

(def latest-date (let [s (env :latest-date)]
                   (if (= s "now")
                     (date/now)
                     (date/parse-local "yyyy/MM/dd HH:mm:ss" s))))

;; config register-date pattern is 'yyyy/MM/dd'
;; ZonedDateTime needs Time (HH:mm:ss)
(def register-date
  (date/parse-local "yyyy/MM/dd HH:mm:ss"
                    (str (env :register-date) " 00:00:00")))

(def interval-month (Long/parseLong (env :interval-month)))

(def work-dir (env :work-dir))

(def log-dir (env :log-dir))

(def json-file (env :json-file))

(def sqlite-file (env :sqlite-file))

(def template-file (env :template-file))

(def html-dir (env :html-dir))