(ns net.svard.sl-dashboard.mongo-repository 
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [monger.core :as mg]
            [monger.collection :as mc]
            [net.svard.sl-dashboard.repository :refer [Repository]])
  (:import [org.bson.types ObjectId]))

(defonce coll "stops")

(defrecord Database [host port dbname] 
  component/Lifecycle
  (start [component] 
    (let [conn (mg/connect {:host host :port port})
          db (mg/get-db conn dbname)]
      (log/info "Starting database")
      (assoc component :db db :conn conn)))
  (stop [{:keys [conn] :as component}]
    (log/info "Stopping database")
    (try
      (mg/disconnect conn)
      (catch Throwable t (log/error "Failed to stop database")))
    (assoc component :db nil :conn nil)))

(defrecord MongoRepository [db]
  Repository
  (save [_ item]
    (if (:_id item)
      (do
        (->> (update-in item [:_id] #(ObjectId. %))
             (mc/save db coll))
        (:_id item))
      (let [id (ObjectId.)]
        (as-> item $
          (assoc $ :_id id)
          (mc/save db coll $))
        (str id))))
  (delete [_ id] 
    (mc/remove-by-id db coll (ObjectId. id)))
  (get-all [_]
    (->> (mc/find-maps db coll) 
         (map #(update % :_id str))))
  (get-by-id [_ id]
    (-> (mc/find-map-by-id db coll (ObjectId. id))
        (update :_id str))))

(defn database
  [{:keys [mongodb]}]
  (map->Database mongodb))

(defn mongo-repo
  [db]
  (->MongoRepository db))
