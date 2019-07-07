## Synopsis
# Building a Small Bank with Clojure

Created: Thu 18 Oct 03:34:12 -03 2018
Application Version: 0.0.1-alpha

An article was published explaining:

 * [https://www.javacodegeeks.com/2019/02/small-bank-application-clojure.html](https://www.javacodegeeks.com/2019/02/small-bank-application-clojure.html)
 * [https://dzone.com/articles/building-a-small-bank-with-clojure](https://dzone.com/articles/building-a-small-bank-with-clojure)
 
This file is intended to demonstrate the execution and testing of the Duck application. Application created for studies and 
tests with clojure language.

#### Provided Mechanisms ####

It must comprise a HTTP Server with two endpoints:

One to insert a new monetary transaction, money in or out, for a given user;
One to return a user's current balance.

* Receive a command and return the response;
* Manage multiple concurrent connections using thread.
* It must not be possible to withdraw money for a given user when they don't have enough balance;
* You should take concurrency issues into consideration;
* It must be executable in Unix, Linux & MacOS machines;

_________________________________________________________________________________

CONTENTS:

        (sec-01) APPLICATION EXECUTION PLATFORM
        (sec-02) INSTALLING THE REQUERIED PACKAGES
        (sec-03) RUNNING THE DUCK APPLICATION
        (sec-04) DETAILS OF EACH APPLICATION FEATURE
        
__________________________________________________________________________

(sec-01) APPLICATION EXECUTION PLATFORM
__________________________________________________________________________

The requirements of the application is to run on Linux and Mac/OS machines.
It was not possible to test Mac/OS because there was no machine available
with this system.

The application was developed using the FreeBSD-11 operating system and was
tested on a virtual machine using Linux/Ubuntu-18.

The application was developed using Clojure the versions of the packages
are:

* clojure-1.9.0.381 - Dynamic programming language for the JVM;
* leiningen-2.8.1 - Automate Clojure projects.

Main Clojure libraries/plugins used:

* Compojure - Is a small routing library for Ring that allows web
  applications to be composed of small, independent parts;
  (https://github.com/weavejester/compojure)

* DataJSON - JSON parser/generator to/from Clojure data structures;
  (https://github.com/clojure/data.json)

* Ring-Mock - Is a library for creating Ring request maps for testing
  purposes; (https://github.com/ring-clojure/ring-mock)

* Clj-Time - A date and time library for Clojure, wrapping the Joda Time
  library; (https://github.com/clj-time/clj-time)

* Lein-Midje - This plugin runs both Midje and clojure.test tests;
  (https://github.com/marick/lein-midje)

* Codox - A tool for generating API documentation from Clojure or
  ClojureScript source code; (https://github.com/weavejester/codox)

* Cljfmt - Is a tool for formatting Clojure code.
  (https://github.com/weavejester/cljfmt)

__________________________________________________________________________

(sec-02) INSTALLING THE REQUERIED PACKAGES
__________________________________________________________________________

To install the required packages see the topics of each system.

* FREEBSD-11

```shell
   pkg install clojure leiningen
   pkg install bash
```

* LINUX/UBUNTU-18
   (https://lispcast.com/clojure-ubuntu/)

__________________________________________________________________________

(sec-03) RUNNING THE MCDUCK APPLICATION
__________________________________________________________________________

Important: Version JDK used to build this project is jdk1.8.0.

To run the project and perform the tests please follow the steps below:

1) To run the project please unzip the file:

```shell
   tar xfv duck-0.0.1-alpha.tar.bz2
```

2) To run and install the required packages please run:

```shell
   cd duck-0.0.1-alpha
   ./launch-app
```

3) At another terminal run the unit tests:

```shell
   lein midje
```

4) To take a stress test perform:

```shell
   ./launch-curl
```

5) Checking the formatting of the code:

```shell
   lein cljfmt check
```

6) Generating HTML documentation:

```shell
   lein codox
```

In the case of HTML documentation it is available by default in the folder
of the project itself located at: (target/doc/index.html)

__________________________________________________________________________

(sec-04) DETAILS OF EACH APPLICATION FEATURE
__________________________________________________________________________

To make the tests more detailed is used the command curl in the terminal
it is possible to use the browser as well.

ATTENTION:

  o In order not to interfere with the previous tests please stop the application
  server and start again.

  o It is important to put the date of the day you are doing the transaction,
  field (date) in pattern DD/MM/YYYY.

The idea is to do a step-by-step for understanding, copy and paste the commands
in the terminal.

 1) Entering a new account with the starting balance of (150.90). The API returns
   a message of success: Successful transaction.

```shell
curl -H "Content-Type: application/json" -X POST -d '{"amount":150.90, "date":"18/10/2018", "account":1112, "description":"Credit 1"}' http://127.0.0.1:3000/transaction/add
```
  API-RESP: Successful transaction

2) Checking available balance for account (1112):

```shell
curl -H "Content-Type: application/json" -X GET http://127.0.0.1:3000/account/1112/balance
```
  API-RESP: {"account":1112,"date":"2018-10-18T03:05:55.077-03:00","balance":150.9}

3) Cash out from account 1112, Eg: pulling out the (50) value from the account:

```shell
curl -H "Content-Type: application/json" -X POST -d '{"amount":-50, "date":"18/10/2018", "account":1112, "description":"Debit 1"}' http://127.0.0.1:3000/transaction/rm
```
  API-RESP: Successful transaction

4) Check again your account balance, step 2:

```shell
curl -H "Content-Type: application/json" -X GET http://127.0.0.1:3000/account/1112/balance
```
  API-REST: {"account":1112,"date":"2018-10-18T03:11:54.554-03:00","balance":100.9}
