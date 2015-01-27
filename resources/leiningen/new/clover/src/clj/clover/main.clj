(ns {{ns}}.main
  (:gen-class)
  (:require [aleph.http :as http]
            [{{ns}}.server :as server]
            [{{ns}}.components.http :refer [new-http]]
            [com.stuartsierra.component :as component]))

(defn- env-true? [k]
  (= (System/getenv k) "true"))

(defn- env-int [k]
  (if-let [port-string (System/getenv k)]
    (Integer/parseInt port-string)))

(defn- config []
  (let [port (or (env-int "PORT") 3000)
        freeze-assets? (env-true? "FREEZE_ASSETS")
        debug-exceptions? (env-true? "DEBUG_EXCEPTIONS")]
    {:http {:port port
            :freeze-assets? freeze-assets?
            :debug-exceptions? debug-exceptions?}}))

(defn new-system [config]
  (component/system-map
   :http (new-http (:http config))))

(defn -main []
  (-> (config)
      new-system
      component/start-system))
