(ns {{ns}}.server
  (:require [compojure.core :refer [routes GET]]
            [compojure.route :as route]
            [hiccup.element :refer [javascript-tag]]
            [hiccup.page :refer [html5 include-js]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :as strategies]
            [optimus.hiccup :refer [link-to-js-bundles]]
            [prone.middleware :as prone]))

(defn- get-assets []
  (assets/load-bundle "public" "prod.js" ["/js/app.js"]))

(defn- advanced-js [req]
  [(link-to-js-bundles req ["prod.js"])])

(defn- expanded-js [req]
  [(include-js "//cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react.js")
   (include-js "/js/out/goog/base.js")
   (include-js "/js/app.js")
   (javascript-tag "goog.require(\"{{ns}}.boot\")")])

(defn- asset-optimizations [assets options]
  (-> assets
      (optimizations/concatenate-bundles)
      (optimizations/add-cache-busted-expires-headers)
      (optimizations/add-last-modified-headers)))

(defn render-layout [js]
  (html5
   [:head
    [:title "FIXME: {{name}}"]]
   [:body (cons [:main#app] js)]))

(defn- make-routes [freeze-assets?]
  (routes
   (GET "/" req (render-layout (if freeze-assets?
                                 (advanced-js req)
                                 (expanded-js req))))
   (route/not-found "Not found! <a href=\"Back\"></a>")))

(defn make-handler [{:keys [debug-exceptions? freeze-assets?]}]
  (let [asset-strategy (if freeze-assets?
                         strategies/serve-frozen-assets
                         strategies/serve-live-assets)]
    (-> (make-routes freeze-assets?)
        (optimus/wrap get-assets asset-optimizations asset-strategy)
        (cond-> debug-exceptions? prone/wrap-exceptions)
        (wrap-defaults site-defaults))))
