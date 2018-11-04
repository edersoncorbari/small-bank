;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.utility
  "Namespace containing utility and validations of date, numbers, etc..."
  {:doc/format :markdown}
  (:require [clj-time.core :as time]
            [clj-time.format :as format]))

(defn date?
  "Check the date pattern by applying the regular expression.
  Default: DD/MM/YYYY."
  [s]
  (and (not (nil? s))
       (not (nil? (re-matches  #"[0-9]{2}\/[0-9]{2}\/[0-9]{4}" s)))))

(defn now-gmt-sp
  "Converts the date to UTC (-3) default: São Paulo / Brazil."
  []
  (time/to-time-zone (time/now) (time/time-zone-for-offset -3)))

(def date-formatter
  "It formats the date to the DD/MM/YYYY format."
  (format/formatter "dd/MM/yyyy"))

(defn date-formatter-2
  "Format the date to the default DD/MM/YYYY using the local-now
  as param param."
  [x]
  (format/unparse (format/formatter "dd/MM/yyyy") (x)))

(defn json-standardized?
  "Check the user-submitted JSON fields if they are in the correct pattern.
  **Fields:**

  * account
  * amount
  * description
  * date
  "
  {:doc/format :markdown}
  [s]
  (and (not (nil? s))
       (contains? s "account")
       (contains? s "amount")
       (contains? s "description")
       (contains? s "date")
       (number? (s "account"))
       (number? (s "amount"))
       (date? (s "date"))))

(defn numeric?
  "Checks if the value is numeric."
  {:doc/format :markdown}
  [s]
  (and (not (nil? s))
       (if-let [s (seq s)]
         (let [s (if (= (first s) \-) (next s) s)
               s (drop-while #(Character/isDigit %) s)
               s (if (= (first s) \.) (next s) s)
               s (drop-while #(Character/isDigit %) s)]
           (empty? s)))))
