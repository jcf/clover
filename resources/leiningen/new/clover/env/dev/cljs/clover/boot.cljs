(ns {{ns}}.boot
  (:require [{{ns}}.client :as client]
            [clojure.string :as str]
            [figwheel.client :as fw]
            [weasel.repl :as weasel]))

;; TODO Pull server :port from configuration
(fw/start {:websocket-url "ws://localhost:3449/figwheel-ws"
           :url-rewriter #(str/replace % ":3449" ":3000")})

(weasel/connect "ws://localhost:9001")

(client/start)
