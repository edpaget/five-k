(defproject five-k "0.1.0-SNAPSHOT"
  :description "Marathon is lot of work, start with a 5K."
  :url "https://github.com/prasincs/five-k"
  :license {:name "MIT"
            :url "https://github.com/prasincs/five-k/LICENSE"}
  :dependencies [[cheshire "5.5.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [compojure "1.4.0"]
                 [curator "0.0.6"]
                 [edpaget/clj-mesos "0.22.1-SNAPSHOT"]
                 [http-kit "2.1.19"]
                 [liberator "0.13"]
                 [org.clojure/clojure "1.6.0"]
                 [ring/ring-json "0.4.0"]
                 [selmer "0.9.1"]]
  :target-path "target/%s"
  :profiles {:dev {:dependencies [[alembic "0.3.2"]
                                  [org.clojure/tools.namespace "0.2.7"]]
                   :source-paths ["dev"]}
             :user {:plugins [[cider/cider-nrepl "0.9.1"]
                              [refactor-nrepl "1.1.0"]
                              [lein-gorilla "0.3.4"]]}
             :uberjar {:aot :all}})
