(defproject clover/lein-template "0.1.0"
  :description "A Leiningen template for an instantly deployable Reagent web app"
  :url "https://github.com/listora/clover"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :deploy-repositories [["releases" :clojars]]
  :eval-in-leiningen true
  :profiles {:dev {:dependencies [[me.raynes/fs "1.4.6"]]}})
