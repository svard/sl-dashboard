(ns net.svard.sl-dashboard.repository
  (:require [clojure.spec.alpha :as s]))

(s/def ::name string?)
(s/def ::siteid int?)
(s/def ::type string?)
(s/def ::timeWindow int?)
(s/def ::stop (s/keys :req-un [::name ::siteid]))
(s/def ::item (s/keys :req-un [::stop ::type ::timeWindow]))

(defprotocol Repository
  "Protocol for persisting domain objects"
  (save [repo item])
  (delete [repo id])
  (get-all [repo])
  (get-by-id [repo id]))
