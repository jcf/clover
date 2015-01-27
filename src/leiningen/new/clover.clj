(ns leiningen.new.clover
  (:require [leiningen.core.main :as main]
            [leiningen.new.templates :refer [->files
                                             name-to-path
                                             project-name
                                             sanitize-ns
                                             renderer]]))

(def render (renderer "clover"))

(def clojurish-templates
  "All templates, with the exception of `gitignore` which needs to be 'hidden'
  from git."
  ["env/dev/clj/clover/repl.clj"
   "env/dev/cljs/clover/boot.cljs"
   "env/prod/clj/clover/system.clj"
   "env/prod/cljs/clover/boot.cljs"
   "phantomjs/console-polyfill.js"
   "phantomjs/es5-sham.js"
   "phantomjs/es5-shim.js"
   "project.clj"
   "src/clj/clover/components/http.clj"
   "src/clj/clover/main.clj"
   "src/clj/clover/server.clj"
   "src/cljs/clover/client.cljs"
   "test/clj/clover/server_test.clj"
   "test/clj/clover/test/markup.clj"
   "test/cljs/clover/client_test.cljs"])

(defn- expand-paths
  "Take a list of template paths, and expand them into a map of destination
  mapped to template path.

  Any instance of `clover` in the template path will be replaced with
  `{{path}}`."
  [paths]
  (->> paths
       (map (juxt #(.replace % "clover" "{{path}}") identity))
       (into {})))

(defn- get-manifest [paths]
  (assoc (expand-paths paths) ".gitignore" "gitignore"))

(defn- render-files
  "Given a list of destinations and template paths, maps over the template paths
  and renders each file using the `clover` renderer."
  [files data]
  (reduce-kv #(assoc %1 %2 (render %3 data)) {} files))

(defn clover
  [name]
  (let [data {:name name
              :ns (sanitize-ns name)
              :path (name-to-path name)
              :project-name (project-name name)}
        files (get-manifest clojurish-templates)]
    (main/info (format "Generating %d files..." (count files)))
    (apply ->files data (render-files files data))))
