(ns {{ns}}.components.http
  (:require [aleph.http :as http]
            [{{ns}}.server :as server]
            [com.stuartsierra.component :as component]))

(defrecord HTTP [port freeze-assets?]
  component/Lifecycle
  (start [component]
    (if (:server component)
      component
      (assoc component :server
             (http/start-server
              (server/make-handler {:freeze-assets? freeze-assets?})
              {:port port}))))
  (stop [component]
    (when-let [server (:server component)]
      (.close server))
    (assoc component :server nil)))

(defn new-http [{:keys [port freeze-assets?]
                 :or {freeze-assets? false}}]
  {:pre [(number? port)]}
  (->HTTP port freeze-assets?))
