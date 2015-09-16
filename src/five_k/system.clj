(ns five-k.system
  (:require [com.stuartsierra.component :as component]
            [five-k.component.executor-driver :refer [new-executor-driver]]
            [five-k.component.scheduler-driver :refer [new-scheduler-driver]]
            [five-k.component.leader-driver :refer [new-leader-driver]]
            [five-k.component.curator :refer [new-curator]]
            [five-k.component.scheduler :refer [new-scheduler]]
            [five-k.executor :refer [executor]]
            [five-k.scheduler :refer [scheduler] :as sched]
            [five-k.example-webserver :as web])
  (:gen-class))

(defn executor-system
  []
  (component/system-map
   :driver (new-executor-driver (executor))))

(defn scheduler-system
  [master n-tasks task-launcher]
  (component/system-map
   :scheduler (new-scheduler n-tasks task-launcher)
   :driver (component/using
            (new-scheduler-driver master)
            [:scheduler])))

(defn ha-scheduler-system
  [master n-tasks exhibitor zk-path task-launcher]
  (component/system-map
   :curator (new-curator exhibitor)
   :scheduler (new-scheduler n-tasks task-launcher)
   :leader-driver (component/using
                   (new-leader-driver zk-path master "five-k" "five-k")
                   [:curator :scheduler])))

(defn -main
  [command-type & [scheduler-type master n-tasks & _]]
  (let [system (condp = [command-type scheduler-type]
                 ["scheduler" "jar"] (scheduler-system master n-tasks sched/jar-task-info)
                 ["scheduler" "ha"] (ha-scheduler-system master n-tasks sched/jar-task-info)
                 ["executor" nil] (executor-system)
                 ["example-webserver" "9090"] (web/start-server 9090))]
    (component/start system)
    (while true
      (Thread/sleep 1000000))))
