(ns {{ns}}.system
  (:require [aleph.http :as http]
            [{{ns}}.server :as server]
            [{{ns}}.components.http :refer [new-http]]
            [com.stuartsierra.component :as component]))

(defn new-system [config]
  (component/system-map
   :http (new-http (:http config))))
