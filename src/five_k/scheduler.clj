(ns five-k.scheduler
  (:require [clj-mesos.scheduler :as mesos]))

(def min-cpu 0.5)
(def min-mem 128.0)

(defn jar-task-info
  [uuid {:keys [slave-id]}]
  [{:name "five-k"
    :task-id uuid
    :slave-id slave-id
    :resources {:cpus min-cpu
                :mem min-mem}
    :executor {:executor-id "five-k-executor"
               :command {:shell true
                         :value "java -jar /vagrant/target/uberjar/five-k-0.1.0-SNAPSHOT-standalone.jar -m five-k.system executor" }}}])


(defn example-webserver-task-info
  [uuid {:keys [slave-id]}]
  [{:name "five-k-webserver"
    :task-id uuid
    :slave-id slave-id
    :resources {:cpus min-cpu
                :mem min-mem}
    :executor {:executor-id "five-k-webserver"
               :command {:shell true
                         :value "java -jar /vagrant/target/uberjar/five-k-0.1.0-SNAPSHOT-standalone.jar -m five-k.system example-webserver 9090" }}}])


(defn resources?
  [{:keys [cpus mem]}]
  (and (>= cpus min-cpu)
       (>= mem min-mem)))

(defn scheduler
  [scheduler-state task-launcher]
  (mesos/scheduler
   (statusUpdate [driver status]
                 ;(println "[DEBUG]" status)
                 (condp = (:state status)
                   :task-running (println status)
                   :task-lost (swap! scheduler-state update-in [:to-launch] inc)
                   (println "[statusUpdate]" status)))
   (resourceOffers [driver offers]
                   (doseq [offer offers]
                     ;(println "[resourceOffers]" offer)
                     (let [uuid (str (java.util.UUID/randomUUID))]
                       (if (and (< 0 (:to-launch @scheduler-state))
                                (resources? (:resources offer)))
                         (let [tasks (task-launcher uuid offer)]
                           (mesos/launch-tasks driver (:id offer) tasks)
                           (swap! scheduler-state update-in [:to-launch] dec))
                         (mesos/decline-offer driver (:id offer))))))))
