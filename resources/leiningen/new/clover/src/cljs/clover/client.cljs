(ns {{ns}}.client
  (:require [reagent.core :as reagent]))

(defn hello-world []
  (let [clicks (reagent/atom 0)]
    (fn []
      [:div.hello
       [:h1 "Hello world!"]
       [:p (str "We've done this " @clicks) " times."]
       [:button {:on-click #(swap! clicks inc)} "Increment"]
       [:button {:on-click #(reset! clicks 0)} "Reset"]])))

(defn start []
  (enable-console-print!)
  (reagent/render
   [hello-world]
   (. js/document (getElementById "app"))))
