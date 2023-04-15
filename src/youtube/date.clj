(ns youtube.date
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [youtube.config :as config]))

(defn format-date [date]
  (str (f/unparse (f/formatter "yyyy-MM-dd") date)
       "T"
       (f/unparse (f/formatter "HH:mm:ss") date)
       "Z"))

(defn dates-between [start end interval]
  (let [s (->> end
               (iterate #(t/minus %1 (t/months interval)))
               (take-while #(t/after? %1 start)))
        v (conj (into [] s) start)]
    (map format-date v)))

(def periods
  (let [dates (dates-between config/register-date
                             config/latest-date
                             config/interval-month)]
    (map vector (next dates) dates)))