(ns {{ns}}.test.markup
  (:require [net.cgrand.enlive-html :as html :refer [html-resource]]))

(defn- parse [^String r]
  (-> r java.io.StringReader. html-resource))

(defn scripts-with-src [r src-fn]
  (html/select (parse r)
               [[:script (html/pred #(some-> % :attrs :src src-fn))]]))

(defn scripts [r]
  (html/select (parse r) [:script]))
