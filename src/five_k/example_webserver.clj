(ns five-k.example-webserver
  (:require [compojure.core :refer [routes GET ANY POST]]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]])
  )



(defn app-routes
  []
  (-> (routes
       (GET "/" [] "Hello from 5K!"))))


(defn start-server
  [port]
  (run-server (app-routes) {:port port}))
