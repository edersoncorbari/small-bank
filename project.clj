(defproject small-bank "0.0.1-alpha"
  :description "Simple Rest API to Checks and Balances (Small-Bank)."
  :min-lein-version "2.1.0"
  :license {:name "BSD License"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [overtone/at-at "1.2.0"]
                 [ring/ring-mock "0.3.1"]
                 [bananaoomarang/ring-debug-logging "1.1.0"]
                 [clj-time "0.13.0"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-midje "3.2.1"]
            [lein-cljfmt "0.6.1"]
            [lein-codox "0.10.5"]]
  :ring {:handler duck.launch/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [midje "1.6.3"]]}})
