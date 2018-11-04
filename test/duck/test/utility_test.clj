;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.test.utility-test
  (:require [midje.sweet :refer :all]
            [duck.utility :as utility]))

(fact (utility/date? nil) => false)

(fact (utility/date? "00002018") => false)

(fact (utility/date? "hello") => false)

(fact (utility/date? "18/09/1950") => true)

(fact (utility/numeric? nil) => false)

(fact (utility/numeric? "hello") => false)

(fact (utility/numeric? "1269AvXXAAAAAADDDCC") => false)

(fact (utility/numeric? "123456789") => true)

(fact (utility/numeric? "-1") => true)

(fact (utility/json-standardized? nil) => false)

(fact (utility/json-standardized? {"amount" 123.40
                                   "account" 1
                                   "date" "18/09/2050"
                                   "description" "Credit"}) => true)

(fact (utility/json-standardized? {"amount" "123.40"
                                   "account" "1"
                                   "date" "18092050"
                                   "description" "Debt"}) => false)

(fact (utility/json-standardized? {"amount" 12.40
                                   "account" 1
                                   "date" "18092050"
                                   "description" "Debt 2"}) => false)

(fact (utility/json-standardized? {"amount" 12.40}) => false)

(fact (utility/json-standardized? {}) => false)
