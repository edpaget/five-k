(defproject five-k "0.1.0-SNAPSHOT"
  :description "Introduction to Mesos Framework using Clojure"
  :url "https://github.com/clj-mesos-workshop/hello-mesos"
  :license {:name "MIT"
            :url "https://github.com/edpaget/hello-mesos/LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [edpaget/clj-mesos "0.22.1-SNAPSHOT"]
                 [curator "0.0.6"]
                 [com.stuartsierra/component "0.2.3"]]
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.7"]]
                   :source-paths ["dev"]}
             :user {:plugins [[refactor-nrepl "1.0.5"]
                              [cider/cider-nrepl "0.9.1"]
                              [refactor-nrepl "1.1.0"]
                              [lein-gorilla "0.3.4"]]}
             :uberjar {:aot :all}})
