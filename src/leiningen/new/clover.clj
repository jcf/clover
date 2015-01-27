(ns leiningen.new.clover
  (:require [leiningen.core.main :as main]
            [leiningen.new.templates :refer [->files
                                             name-to-path
                                             project-name
                                             sanitize-ns
                                             renderer]]))

(def render (renderer "clover"))

(defn clover
  [name]
  (let [data {:name name
              :ns (sanitize-ns name)
              :path (name-to-path name)
              :project-name (project-name name)}]
    (main/info "Generating fresh 'lein new' clover project.")
    (->files data
             ["project.clj" (render "project.clj" data)])))
