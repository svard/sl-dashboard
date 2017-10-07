(ns net.svard.sl-dashboard.sl-api-spec
  (:require [speclj.core :refer :all]
            [net.svard.sl-dashboard.sl-api :refer :all]
            [clojure.java.io :as io]))

(def search-response (slurp (io/resource "search")))
(def departures-response (slurp (io/resource "departures")))

(describe "search stop"
  (it "should create a well formed url"
    (should= "http://example.com?key=abc123&searchstring=foobar" (search-url "http://example.com" "abc123" "foobar")))
  
  (it "should return name and siteid for matching search results"
    (with-redefs [block-read (constantly search-response)] 
      (should= '({:name "Brotorp (Sundbyberg)", :siteid "3470"} {:name "Brotorp (Norrtälje)", :siteid "6822"} {:name "Brotorp (Vallentuna)", :siteid "2473"}) (search "Brotorp")))))

(describe "lookup departures"
  (it "should create a well formed url"
    (should= "http://example.com?key=abc123&siteid=42&timewindow=60" (departures-url "http://example.com" "abc123" "42" "60")))

  (it "should extract data from response"
    (with-redefs [block-read (constantly departures-response)]
      (should= 7 (count (:buses (departures "42" "Buses"))))
      (should= 5 (count (:metros (departures "42" "Metros")))))))
