(defproject clover/lein-template "0.2.0-SNAPSHOT"
  :description "A Leiningen template for an instantly deployable Reagent web app"
  :url "https://github.com/jcf/clover"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :deploy-repositories [["releases" :clojars]]
  :eval-in-leiningen true
  :profiles {:dev {:dependencies [[me.raynes/fs "1.4.6"]]}})
