(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[aleph "0.4.0-alpha9"]
                 [com.stuartsierra/component "0.2.2"]
                 [compojure "1.3.1"]
                 [hiccup "1.0.5"]
                 [optimus "0.15.1"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [prone "0.8.0"]
                 [reagent "0.5.0-alpha"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-defaults "0.1.3"]]

  :plugins [[com.cemerick/clojurescript.test "0.3.1"]
            [lein-cljsbuild "1.0.4"]
            [lein-figwheel "0.2.1-SNAPSHOT"]]

  :clean-targets ^{:protect false} ["resources/public/js"]
  :min-lein-version "2.5.0"
  :repl-options {:init-ns {{ns}}.repl}
  :source-paths ["src/clj" "src/cljs"]
  :test-paths   ["test/clj" "test/cljs"]
  :uberjar-name "{{project-name}}-standalone.jar"

  :profiles
  {:dev {:dependencies [[clojurescript-build "0.1.2-SNAPSHOT"]
                        [com.cemerick/piggieback "0.1.4"]
                        [enlive "1.1.5"]
                        [figwheel "0.2.1-SNAPSHOT"]
                        [figwheel-sidecar "0.2.1-SNAPSHOT"]
                        [leiningen-core "2.5.0"]
                        [org.clojure/tools.namespace "0.2.5"]
                        [reloaded.repl "0.1.0"]
                        [ring/ring-mock "0.2.0"]
                        [weasel "0.5.0"]]
         :source-paths ["env/dev/clj"]
         :cljsbuild
         {:builds {:app
                   {:source-paths ["env/dev/cljs" "src/cljs"]
                    :compiler {:output-to     "resources/public/js/app.js"
                               :output-dir    "resources/public/js/out"
                               :externs       ["react/externs/react.js"]
                               ;; Disabled because it doesn't work without some
                               ;; optimisations. We instead source React from a
                               ;; CDN.
                               ;;
                               ;; :preamble      ["reagent/react.js"]
                               :optimizations :none
                               :source-map    true
                               :pretty-print  true}}
                   :test
                   {:source-paths ["src/cljs" "test/cljs"]
                    :compiler {:output-to  "resources/public/js/test.js"
                               :output-dir "resources/public/js/test"
                               :source-map "resources/public/js/test.js.map"
                               :preamble   ["reagent/react.js"]
                               :optimizations :whitespace
                               :pretty-print true}}}
          :test-commands {"unit" ["phantomjs" :runner
                                  "phantomjs/es5-shim.js"
                                  "phantomjs/es5-sham.js"
                                  "phantomjs/console-polyfill.js"
                                  "resources/public/js/test.js"]}}}

   ;; TODO Test this works!
   :uberjar {:hooks [leiningen.cljsbuild]
             :source-paths ["env/prod/clj"]
             :aot :all
             :omit-source true
             :main {{ns}}.main
             :cljsbuild
             {:builds {:app
                       {:source-paths ["env/prod/cljs"]
                        :compiler {:output-to  "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :externs    ["react/externs/react.js"]
                                   :preamble   ["reagent/react.min.js"]
                                   :optimizations :advanced
                                   :pretty-print false}}}}}})
