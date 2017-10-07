(ns net.svard.sl-dashboard.sl-api
  (:require [aleph.http :as http]
            [clojure.tools.logging :as log]
            [immuconf.config :as config]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [byte-streams :as bs]
            [cheshire.core :as json]))

(def config (config/load (io/resource "config.edn") (io/resource "prod.edn")))

(s/fdef keywordize
  :args (s/cat :key string?)
  :ret keyword?)

(defn keywordize
  "Lower case and keywordize the given string key"
  [key]
  (keyword (.toLowerCase key)))

(defn block-read
  "Synchronously get url"
  [url]
  (-> @(http/get url)
      :body
      bs/to-string))

(s/fdef search-url
  :args (s/and (s/cat :base-url string? :key string? :query string?)
          #(< 0 (count (:base-url %)))
          #(< 0 (count (:key %)))
          #(< 0 (count (:query %))))
  :ret string?)

(defn search-url
  "Formats a url with correct query parameters for searching stops"
  [base-url key query]
  (str base-url "?key=" key "&searchstring=" query))

(s/def ::name string?)
(s/def ::siteid string?)
(s/def ::search-response (s/keys :req-un [::name ::siteid]))

(s/fdef search
  :args (s/and (s/cat :query string?)
          #(< 0 (count (:query %))))
  :ret (s/* ::search-response))

(defn search
  "Returns a sequence of name and site id matching the query"
  [query]
  (let [base-url (get-in config [:sl-api :typeahead :url])
        key (get-in config [:sl-api :typeahead :key])]
    (as-> (block-read (search-url base-url key query)) $      
      (json/parse-string $ keywordize)
      (:responsedata $)
      (filter #(not= (:siteid %) "0") $)
      (map #(select-keys % [:name :siteid]) $))))

(s/def ::latestupdate string?)
(s/def ::displaytime string?)
(s/def ::linenumber string?)
(s/def ::destination string?)
(s/def ::timetabledatetime string?)
(s/def ::expecteddatetime string?)
(s/def ::journeynumber string?)
(s/def ::departure-response (s/keys :req-un [::latestupdate
                                             ::displaytime
                                             ::linenumber
                                             ::destination
                                             ::timetabledatetime
                                             ::expecteddatetime
                                             ::journeynumber]))

(s/fdef departures
  :args (s/cat :siteid string? :type string? :timewindow string?)
  :ret (s/* ::departure-response))

(defn departures-url
  "Formats a url for looking up departures"
  [base-url key siteid timewindow]
  (str base-url "?key=" key "&siteid=" siteid "&timewindow=" timewindow))

(defn departures
  "Looks up departures for the given siteid and transport type"
  ([siteid]
   (departures siteid "Buses"))
  ([siteid type]
   (departures siteid type "20"))
  ([siteid type timewindow]
   (let [base-url (get-in config [:sl-api :departures :url])
         key (get-in config [:sl-api :departures :key])
         keywordized-type (keywordize type)]
     (as-> (block-read (departures-url base-url key siteid timewindow)) $
       (json/parse-string $ keywordize)
       (:responsedata $)
       (select-keys $ [:latestupdate keywordized-type])
       (assoc $ keywordized-type (map #(select-keys % [:displaytime
                                                       :linenumber
                                                       :destination
                                                       :timetabledatetime
                                                       :expecteddatetime
                                                       :journeynumber]) (keywordized-type $)))))))
