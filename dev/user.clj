(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require [clojure.java.io :as io]
            [clojure.java.javadoc :refer [javadoc]]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [alembic.still :refer [lein]]
            [com.stuartsierra.component :as component]
            [simple-marathon .system :as sys]
            [simple-marathon .scheduler :as sched]
            [clojure.java.shell :refer [sh]]))

(def configuration (atom nil))

(defn build-uberjar
  []
  (println "building jar")
  (sh "lein" "uberjar"))


(def system
  "A Var containing an object representing the application under
  development."
  nil)

(defn init
  "Creates and initializes the system under development in the Var
  #'system."
  []
;;  (alter-var-root #'system (constantly (sys/scheduler-system (get-config :mesos-master) 1))))
  ;;(build-uberjar)
  (lein uberjar)
  (alter-var-root #'system (constantly (sys/scheduler-system "zk://10.10.4.2:2181/mesos"
                                                             1
                                                             sched/jar-task-info))))

(defn start
  "Starts the system running, updates the Var #'system."
  []
  (alter-var-root #'system component/start))

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  (alter-var-root #'system component/stop))

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))
