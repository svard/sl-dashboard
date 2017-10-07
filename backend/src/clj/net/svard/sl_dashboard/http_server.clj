(ns net.svard.sl-dashboard.http-server
  (:require [com.stuartsierra.component :as component]
            [aleph.http :as http]
            [clojure.tools.logging :as log]
            [net.svard.sl-dashboard.controller :as ctrl]
            [net.svard.sl-dashboard.mongo-repository :as repository]))

(defrecord HttpServer [port]
  component/Lifecycle
  (start [{:keys [:database] :as component}]
    (let [use-port (Integer/parseInt (or (System/getenv "HTTP_PORT") port))
          repo (repository/mongo-repo (:db database))
          server (http/start-server (ctrl/handler repo) {:port use-port})]
      (log/info "Starting HTTP server on port" use-port)
      (assoc component :http-server server)))

  (stop [{:keys [http-server] :as component}]
    (log/info "Stopping HTTP server")
    (.close http-server)
    (assoc component :http-server nil)))

(defn create
  "Creates an Aleph http server instance"
  [{:keys [http]}]
  (map->HttpServer http))
