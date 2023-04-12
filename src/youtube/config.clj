(ns youtube.config
  (:require [environ.core :refer [env]]
            [clj-time.format :as f]))

(def database-url (env :database-url))

(def query-file (env :query-file))

(def api-key (env :api-key))

(def channel-id (env :channel-id))

(def register-date (f/parse (f/formatter "yyyy/MM/dd") (env :register-date)))

(def interval-month (Long/parseLong (env :interval-month)))

(def work-dir (env :work-dir))

(def log-dir (env :log-dir))

(def json-file (env :json-file))

(def template-file (env :template-file))

(def html-file (env :html-file))