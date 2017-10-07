(defproject sl-dashboard "1.0.2-SNAPSHOT"
  :description "A SL comuter dashboard"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main net.svard.sl-dashboard.core
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [com.stuartsierra/component "0.3.2"]
                 [aleph "0.4.3"]
                 [compojure "1.6.0"]
                 [liberator "0.15.1"]
                 [cheshire "5.8.0"]
                 [levand/immuconf "0.1.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/test.check "0.10.0-alpha2"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [ring/ring-defaults "0.3.1"]
                 [ring-middleware-format "0.7.2"]
                 [com.novemberain/monger "3.1.0"]]
  :profiles {:dev {:dependencies [[speclj "3.3.2"]]
                   :resource-paths ["spec/resources"]}
             :uberjar {:aot [net.svard.sl-dashboard.core]}}
  :plugins [[speclj "3.3.2"]
            [cider/cider-nrepl "0.15.1"]
            [io.sarnowski/lein-docker "1.1.0"]]
  :source-paths ["src/clj"]
  :test-paths ["spec/clj"]
  :docker {:image-name "svard/sl-dashboard"}
  :release-tasks [["change" "version" "leiningen.release/bump-version" "release"]
                  ["clean"]
                  ["uberjar"]
                  ["docker" "build"]
                  ["change" "version" "leiningen.release/bump-version"]])
