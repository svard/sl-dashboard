(ns net.svard.sl-dashboard.core
  (:require [immuconf.config :as config]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [net.svard.sl-dashboard.http-server :as http-server]
            [net.svard.sl-dashboard.mongo-repository :as repository])
  (:gen-class))

(def config (config/load (io/resource "config.edn") (io/resource "prod.edn")))

(def system
  (component/system-map
    :database (repository/database config)
    :http-server (component/using (http-server/create config) [:database])))

(defn -main
  [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(alter-var-root #'system component/stop)))
  (alter-var-root #'system component/start)
  @(promise))
