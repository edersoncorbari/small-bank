;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.test.balance-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as time]
            [clj-time.format :as format]
            [clojure.data.json :as json]
            [duck.balance :as balance]
            [duck.utility :as utility]))

(let [date-old (format/parse utility/date-formatter "01/01/1850")
      date-past (format/parse utility/date-formatter "18/09/1985")
      date-future (format/parse utility/date-formatter "22/10/2085")]
  (fact (balance/wait-commit-transaction []) => [])
  (fact (count (balance/wait-commit-transaction [{:date date-old}])) => 0)
  (fact (count (balance/wait-commit-transaction [{:date date-old}
                                                 {:date date-past}
                                                 {:date date-future}])) => 1))

(let [date-future-1 (format/parse utility/date-formatter "01/12/2019")
      date-future-2 (format/parse utility/date-formatter "25/12/2019")
      date-old-1 (format/parse utility/date-formatter "05/10/1992")
      date-old-2 (format/parse utility/date-formatter "25/10/1992")]

  (fact (balance/updated-balance (bigdec 0.0) []) => (bigdec 0.0))
  (fact (balance/updated-balance (bigdec 115000.0) []) => (bigdec 115000.0))

  (fact (balance/updated-balance (bigdec 25.5) [{:date date-future-1
                                                 :amount (bigdec 2.0)}
                                                {:date date-future-1
                                                 :amount (bigdec 1.0)}]) => (bigdec 28.5))

  (fact (balance/updated-balance (bigdec 123.3) [{:date date-old-1
                                                  :amount (bigdec 20.0)}
                                                 {:date date-old-1
                                                  :amount (bigdec -1.0)}]) => (bigdec 142.3)))

;(println (balance/balance-verification (:amount (bigdec 2.0))))
