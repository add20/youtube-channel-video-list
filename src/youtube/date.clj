(ns youtube.date
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [youtube.config :as config]))

(defn format-date [date]
  (str (f/unparse (f/formatter "yyyy-MM-dd") date)
       "T"
       (f/unparse (f/formatter "HH:mm:ss") date)
       "Z"))

(def days (let [s (->> config/latest-date
                       (iterate #(t/minus %1 (t/months config/interval-month)))
                       (take-while #(t/after? %1 config/register-date)))
                v (conj (into [] s) config/register-date)]
            (map format-date v)))

(def periods (map vector (next days) days))