(ns youtube.date
  (:import (java.time LocalDateTime
                      ZonedDateTime
                      ZoneId)
           (java.time.format DateTimeFormatter)))

(defn now []
  (ZonedDateTime/now))

(defn parse-local [pattern date-str]
  (-> (LocalDateTime/parse date-str (DateTimeFormatter/ofPattern pattern))
      (ZonedDateTime/of (ZoneId/systemDefault))))

;; format example
;; https://docs.oracle.com/javase/jp/8/docs/api/java/time/format/DateTimeFormatter.html
;; 2023-04-22T14:26:00+09:00
;; 2023-04-22T05:26:00Z
(def ^:private youtube-format (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ssXXX"))

(defn from-youtube-format [^String youtube-date]
  (-> youtube-date
      (ZonedDateTime/parse youtube-format)
      (.withZoneSameInstant (ZoneId/systemDefault))
      (.format (DateTimeFormatter/ofPattern "yyyy/MM/dd HH:mm:ss"))))

(defn- to-youtube-format [^ZonedDateTime zoned-date]
  (-> zoned-date
      (.withZoneSameInstant (ZoneId/of "UTC"))
      (.format youtube-format)))

(defn- dates-between [start end interval]
  (let [s (->> end
               (iterate #(.minusMonths %1 interval))
               (take-while #(.isAfter %1 start)))
        v (conj (into [] s) start)]
    (map to-youtube-format v)))

(defn periods [register-date latest-date interval-month]
  (let [dates (dates-between register-date
                             latest-date
                             interval-month)]
    (map vector (next dates) dates)))