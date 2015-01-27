(ns {{ns}}.repl
  (:require [cemerick.piggieback :as piggieback]
            [{{ns}}.components.http :refer [new-http]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [leiningen.core.main :as lein]
            [reloaded.repl :refer [system init start stop go reset]]
            [figwheel-sidecar.auto-builder :as fig-auto]
            [figwheel-sidecar.core :as fig]
            [clojurescript-build.auto :as cljs-auto]
            [weasel.repl.websocket :as weasel]
            [com.stuartsierra.component :as component]))

(defrecord Figwheel [builds]
  component/Lifecycle
  (start [component]
    (if (and (:server component) (:builder component))
      component
      (let [server (fig/start-server {})
            config {:builds builds
                    :figwheel-server server}]
        (assoc component
               :server server
               :builder (fig-auto/autobuild* config)))))
  (stop [component]
    (when-let [server (:server component)]
      (fig/stop-server server))
    (when-let [builder (:builder component)]
      (cljs-auto/stop-autobuild! builder))
    (assoc component
           :builder nil
           :server nil)))

;; TODO Keep in sync with cljsbuild
(defn new-figwheel []
  (->Figwheel [{:source-paths ["env/dev/cljs" "src/cljs"]
                :build-options {:id "app"
                                :output-to "resources/public/js/app.js"
                                :output-dir "resources/public/js/out"
                                :externs ["react/externs/react.js"]
                                :optimizations :none
                                :source-map true
                                :pretty-print true}}]))

(defn new-system [config]
  (-> (component/system-map
       :http (new-http (:http config))
       :figwheel (new-figwheel))
      (component/system-using
       {:http [:figwheel]})))

(defn browser-repl []
  (piggieback/cljs-repl :repl-env (weasel/repl-env :ip "0.0.0.0" :port 9001)))

(reloaded.repl/set-init! #(new-system {:http {:port 3000
                                              :freeze-assets? false
                                              :debug-exceptions? true}}))
