;; Author:    Ederson Corbari <ecorbari at protonmail.com> 
;; Created:   05.11.2018
(ns duck.balance
  "Here is the logic of the Checks/Balance where the user can register monetary
  values, update and debit following:

  It must comprise a HTTP Server with two endpoints:

  * One to insert a new monetary transaction, money in or out, for a given user;
  * One to return a user's current balance.

  **Requirements:**

  * It must not be possible to withdraw money for a given user when they
    don't have enough balance;
  * You should take concurrency issues into consideration;
  * It must be executable in Linux & MacOS machines;
  * You don't have to worry about persists; it's fine to use in-process,
    in-memory storage."
  {:doc/format :markdown}
  (:require [clj-time.core :as time]
            [clj-time.format :as format]
            [clojure.data.json :as json]
            [duck.utility :as utility]))

(def ^:private persist
  "Create an atom for the store/persist that can be used shared.

  See: https://clojure.org/reference/atoms."
  (atom {}))

(def ^:private next-free-transaction-id
  "Create an atom for the ID's of accounts that can be shared.

  See: https://clojure.org/reference/atoms."
  (atom 0))

(defn operations-analyze
  "Comparison of ID's if they are positive."
  [x y]
  (if < (x :id) (y :id)))

(defn balance-verification
  "Verification if balance is negative or positive."
  [x]
  (cond
    (> 0 (:amount x)) :negative
    (< 0 (:amount x)) :positive
    (= 0 (:amount x)) :zero))

(defn wait-commit-transaction
  "Wait to commit by making a filter on the date."
  [commit-transaction]
  (filter #(time/after? (% :date) (utility/now-gmt-sp)) commit-transaction))

(defn updated-balance
  "The account balance is updated."
  [outdated-balance commit-transaction]
  (+ outdated-balance (apply + (map (fn [operation]
                                      (if (time/equal? (operation :date)
                                                       (utility/now-gmt-sp))
                                        (bigdec 0.0)
                                        (operation :amount)))
                                    commit-transaction))))

(defn request->operation
  "Create a JSON and log the operation by checking the next available
  account ID."
  [request-data]
  {:id (swap! next-free-transaction-id inc)
   :date (format/parse utility/date-formatter (request-data "date"))
   :amount (bigdec (request-data "amount"))
   :description (request-data "description")})

(defn create-account-does-not-exist
  "Checking if account already exists if it does not exist creates a new one."
  [account]
  (if (not (contains? @persist account))
    (swap! persist assoc account {:done (sorted-set-by operations-analyze)
                                  :due (sorted-set-by operations-analyze)
                                  :balance (bigdec 0.0)})))

(defn update-account [account]
  "It updates the data in a given account."
  (let [entry-transaction (@persist account)
        commit-transaction (get-in @persist [account :due])]
    (if (and (not (empty? commit-transaction))
             (not (time/after? ((first commit-transaction) :date) (utility/now-gmt-sp))))
      (swap! persist assoc account {:due (wait-commit-transaction commit-transaction)
                                    :balance (updated-balance (entry-transaction :balance)
                                                              commit-transaction)}))))

(defn log-transaction
  "Perform the transaction and save the data.

  **See:**

  * [[request/operation]].
  "
  {:doc/format :markdown}
  [request-data]
  (let [account (request-data "account")
        operation (request->operation request-data)]
    (create-account-does-not-exist account)
    (swap! persist update-in [account :due] conj operation)
    (str ":> Successful transaction\n")))

(defn get-balance [account]
  "Check the available balance for an account."
  (create-account-does-not-exist account)
  (update-account account)
  (json/write-str {:account account
                   :date (str (utility/now-gmt-sp))
                   :balance (get-in @persist [account :balance])}))
