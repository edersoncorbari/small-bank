;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.test.launch-test
  (:use [duck.launch])
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [compojure.response :as response]
            [compojure.coercions :as coercions]
            [clojure.data.json :as json]
            [clj-time.core :as time]
            [clj-time.local :as timelocal]
            [clj-time.format :as format]
            [duck.balance :as balance]
            [duck.utility :as utility]))

(defn dt-format [x]
  (format/unparse (format/formatter "dd/MM/yyyy") (x)))

(defn add-operation-request [json-map]
  (let [json-body (json/write-str json-map)]
    (app (mock/content-type (mock/body
                             (mock/request :post "/transaction/add")
                             json-body)
                            "application/json"))))

(fact (let [r (app (mock/request :get "/account/1/balance"))
            body (json/read-str (r :body))]
        (and (= (r :status) 200)
             (= (body "balance") 0.0))) => true)

(defn add-operation-request [json-map]
  (let [json-body (json/write-str json-map)]
    (app (mock/content-type (mock/body
                             (mock/request :post "/transaction/rm")
                             json-body)
                            "application/json"))))

(fact (let [r (add-operation-request {})]
        (r :status)) => 400)

(fact (let [r (add-operation-request {"amount" 50000.000
                                      "description" "Credit 1"
                                      "account" 1112
                                      "date" (utility/date-formatter-2 timelocal/local-now)})]
        (r :status)) => 200)

(fact (let [r (app (mock/request :get "/account/1112/balance"))
            body (json/read-str (r :body))]
        (and (= (r :status) 200)
             (= (body "balance") 50000.000))) => true)

(fact (let [r (add-operation-request {"amount" -40000.000
                                      "description" "Debt 1"
                                      "account" 1112
                                      "date" (utility/date-formatter-2 timelocal/local-now)})]
        (r :status)) => 200)

(defn add-operation-request [json-map]
  (let [json-body (json/write-str json-map)]
    (app (mock/content-type (mock/body
                             (mock/request :post "/transaction/rm")
                             json-body)
                            "application/json"))))

(fact (let [r (app (mock/request :get "/account/1112/balance"))
            body (json/read-str (r :body))]
        (and (= (r :status) 200)
             (= (body "balance") 10000.000))) => true)

(fact (let [r (add-operation-request {"amount" 1000.00
                                      "description" "Credit 1"
                                      "account" 1113
                                      "date" (utility/date-formatter-2 timelocal/local-now)})]
        (r :status)) => 200)

(fact (let [r (app (mock/request :get "/account/1113/balance"))
            body (json/read-str (r :body))]
        (and (= (r :status) 200)
             (= (body "balance") 1000.00))) => true)

(fact (let [r (add-operation-request {"amount" -1000.00
                                      "description" "Debt 1"
                                      "account" 1113
                                      "date" (utility/date-formatter-2 timelocal/local-now)})]
        (r :status)) => 200)

(defn add-operation-request [json-map]
  (let [json-body (json/write-str json-map)]
    (app (mock/content-type (mock/body
                             (mock/request :post "/transaction/rm")
                             json-body)
                            "application/json"))))

(fact (let [r (app (mock/request :get "/account/1113/balance"))
            body (json/read-str (r :body))]
        (and (= (r :status) 200)
             (= (body "balance") 0.0))) => true)
