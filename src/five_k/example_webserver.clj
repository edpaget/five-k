(ns five-k.example-webserver
  (:require [compojure.core :refer [routes GET ANY POST]]
            [clj-mesos.executor :as mesos]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]))




(defn app-routes
  []
  (-> (routes
       (GET "/" [] "Hello from 5K!"))))


(defn start-server
  [port]
  (run-server (app-routes) {:port port}))

(defn executor
  []
  (mesos/executor
   (launchTask [driver task-info]
               (start-server 9090)
               (mesos/send-status-update driver {:task-id (:task-id task-info)
                                                 :state :task-running}))
   (registered [driver executor-info framework-info slave-info]
               (println slave-info))))
