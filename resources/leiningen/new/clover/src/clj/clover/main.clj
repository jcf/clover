(ns {{ns}}.main
  (:gen-class)
  (:require [aleph.http :as http]
            [{{ns}}.server :as server]
            [{{ns}}.components.http :refer [new-http]]
            [com.stuartsierra.component :as component]))

(defn- true? [k]
  (= (System/getenv k) "true"))

(defn- int [k]
  (if-let [port-string (System/getenv k)]
    (Integer/parseInt port-string)))

(defn- config []
  (let [port (or (int "PORT") 3000)
        freeze-assets? (true? "FREEZE_ASSETS")
        debug-exceptions? (true? "DEBUG_EXCEPTIONS")]
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
