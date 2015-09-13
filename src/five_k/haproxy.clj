(ns
    five-k.haproxy
    "Update the HA-Proxy configuration"
    (:require [selmer.parser :as selmer]
              [clojure.java.shell :refer [sh]]))

(def haproxy-template-path "haproxy.cfg.tmpl")

(def haproxy-config-system-path "/etc/haproxy/haproxy.cfg")

(def services (atom [{:name "backend_five_k"
                              :servers [{:name "slave_0"
                                         :address "10.10.4.10"
                                         :port 8080}
                                        {:name "slave_1"
                                         :address "10.10.4.11"
                                         :port 8080}]}]))

(defn update-service! [name servers]
  (let [without-service (filter #(not= (:name %)) @services)]
    (reset! services (conj without-service {:name name
                                            :servers servers}))))

(defn generate-config [path]
  (spit path
        (selmer/render-file haproxy-template-path {:default-service (-> @services
                                                                        first
                                                                        :name)
                                                   :services @services})))

(defn reload []
  ;; reload haproxy
  ;; correct way is hard.. just going to assume it runs as vagrant user
  (sh "sudo" "service" "haproxy" "reload")
  )

(defn update-service []
  ;; Change the file in the system and reload ha-proxy
  (let [path (str "/tmp/haproxy.cfg." (System/currentTimeMillis))
        _ (generate-config path)
        _ (sh "sudo" "cp" haproxy-config-system-path (str haproxy-config-system-path ".bak"))
        _ (sh "sudo" "mv" path haproxy-config-system-path)]
    (reload)))
