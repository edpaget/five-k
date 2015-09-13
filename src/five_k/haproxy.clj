(ns
    "Update the HA-Proxy configuration"
  five-k.haproxy
  (:require [selmer.parser :as selmer]))

(def haproxy-template-path "/haproxy.cfg.tmpl")

(def haproxy-config-system-path "/etc/haproxy.cfg")

(defn generate-config [template path & {:keys [content]
                                        :or {:content ""}}]
  )

(defn reload []
  ;; reload haproxy
  )

(defn update-service [service-name hosts-spec]
  ;; Change the file in the system and reload ha-proxy
  )
