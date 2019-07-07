;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.launch
  "Namespace of the Launch Duck Application **Checks and Balances (Small-Bank)**."
  {:doc/format :markdown}
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [clojure.data.json :as json]
            [duck.balance :as balance]
            [duck.utility :as utility]))

(defn ^:private msg-index
  "Print a welcome message on the home page."
  []
  (str "*** Simple Rest API to Check/Balance Small-Bank ***\n\n"
       "Please use the (curl-api-test) script for testing.\n"))

(defn ^:private msg-not-found
  "Prints a message to the URL user (route) not found."
  [] (str "Ops... Not Found!\n"))

(defn ^:private msg-bad-request
  "Prints a message to the unsuccessful request user."
  [] (str "Ops... Bad Request!\n"))

(defn ^:private transaction
  "Starts a transaction to credit or withdraw money to an account."
  [request]
  (let [data (try
               (json/read-str (slurp (get-in request [:body])))
               (catch Exception e nil))]
    (if (utility/json-standardized? data)
      {:status 200
       :body (balance/log-transaction data)}
      {:status 400
       :body (msg-bad-request)})))

(defroutes app-routes
  "Starts URL routes. **Compojures** default method."
  {:doc/format :markdown}

  ;; Route: with URL starts prints a welcome message.
  (GET "/" [] (msg-index))

  ;; Route: add transactions (credit) to an account or (cashout),
  ;; if it exists it is created.
  (POST "/transaction/add" request (transaction request))
  (POST "/transaction/rm" request (transaction request))

  ;; Route: that checks the balances that were added in the transaction.
  (GET "/account/:account{[0-9]+}/balance" [account]
    (if (utility/numeric? account)
      (let [account (Long/parseLong account)]
        {:status 200
         :body (balance/get-balance account)})
      {:status 400
       :body (msg-bad-request)}))

  ;; Route: a URL not found (404).
  (route/not-found
   {:status 404
    :body (msg-not-found)}))

(def app
  "Starts Server Application."
  (wrap-defaults app-routes api-defaults))
