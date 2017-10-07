(ns net.svard.sl-dashboard.controller
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE] :as compojure]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.format-params :refer [wrap-restful-params]]
            [clojure.tools.logging :as log]
            [clojure.spec.alpha :as s]
            [net.svard.sl-dashboard.sl-api :as api]
            [net.svard.sl-dashboard.repository :as repo]))

(defn wrap-repository
  [handler repo]
  (fn [req]
    (handler (assoc req :repository repo))))

(defresource search [{:keys [params]}]
  :available-media-types ["application/json"]

  :handle-ok (fn [_]
               (log/debug "Received search request with params" params)
               (let [{:keys [query]} params] 
                 (api/search query))))

(defresource departures [{:keys [params]}]
  :available-media-types ["application/json"]

  :handle-ok (fn [_]
               (log/debug "Received departures request with params" params)
               (let [{:keys [siteid timewindow type] :or {timewindow "20" type "Buses"}} params] 
                 (api/departures siteid type timewindow))))

(defresource select [{:keys [params repository body-params] :as req}]
  :available-media-types ["application/json"]

  :allowed-methods [:get :post]

  :malformed? (fn [{{:keys [request-method] :as ctx} :request}]
                (when (= request-method :post)
                  (if (s/valid? ::repo/item body-params)
                    [false {::body-params body-params}]
                    [true {::malformed (s/explain-str ::repo/item body-params)}])))

  :handle-malformed (fn [ctx]
                      (log/debug "Malformed body received" (::malformed ctx))
                      (ring-response
                        {:status 400 :body (::malformed ctx) :headers {"Content-Type" "text/plain"}}))

  :handle-ok (fn [_] 
               (repo/get-all repository))

  :post! (fn [ctx]
           (log/debug "Saving selection to repo" (::body-params ctx))
           {::id (repo/save repository (::body-params ctx))})

  :post-redirect? (fn [ctx]
                    {:location (format "/api/v1/select/%s" (::id ctx))}))

(defresource select-id [{:keys [params repository body-params] :as req}]
  :available-media-types ["application/json"]

  :allowed-methods [:get :put :delete]

  :exists? (fn [_]
             (let [{:keys [id]} params
                   entity (repo/get-by-id repository id)]
               (when (seq entity)
                 {::entity entity})))

  :malformed? (fn [{{:keys [request-method] :as ctx} :request}] 
                (when (= request-method :put)
                  (if (s/valid? ::repo/item body-params)
                    [false {::body-params body-params}]
                    [true {::malformed (s/explain-str ::repo/item body-params)}])))

  :handle-malformed (fn [ctx]
                      (log/debug "Malformed body received" (::malformed ctx))
                      (ring-response
                        {:status 400 :body (::malformed ctx) :headers {"Content-Type" "text/plain"}}))

  :handle-ok (fn [ctx] 
               (::entity ctx))

  :delete! (fn [_]
             (let [{:keys [id]} params]
               (log/debug "Deleting id" id)
               (repo/delete repository id)))

  :put! (fn [ctx]
          (repo/save repository (::body-params ctx))))

(defroutes app-routes
  (compojure/context "/api/v1" []
    (GET "/search" [] search)
    (GET "/departures/:siteid" [] departures)
    (POST "/select" [] select)
    (GET "/select" [] select)
    (GET "/select/:id" [] select-id)
    (PUT "/select/:id" [] select-id)
    (DELETE "/select/:id" [] select-id)))

(defn handler
  [repo]
  (-> app-routes
      (wrap-defaults api-defaults)
      (wrap-restful-params {:formats [:json-kw]})
      (wrap-repository repo)))
