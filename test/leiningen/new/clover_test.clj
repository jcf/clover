(ns leiningen.new.clover-test
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [leiningen.new.clover :refer :all]
            [leiningen.new.templates :refer [*dir*]]
            [me.raynes.fs :refer [temp-dir]]))

(def ^:private app-name
  "example")

(defn- find-files [dir s]
  (let [divider (re-pattern (format "/%s/" s))
        remove-temp-dir #(second (str/split % divider 2))]
    (->> dir
         io/file
         file-seq
         (filter #(.isFile %))
         (map (comp remove-temp-dir str))
         sort)))

(defn generate-project [test-fn]
  (let [sandbox (temp-dir "clover-")]
    (binding [*dir* (str sandbox "/" app-name)]
      (println (format "Generating project in %s..." *dir*))
      (clover app-name)
      (try
        (test-fn)
        (finally
          (when (.isDirectory sandbox)
            (println (format "Deleting project in %s..." *dir*))
            (.delete sandbox)))))))

(use-fixtures :once generate-project)

(deftest test-gitignore-sorted
  (let [ignores (str/split-lines (slurp (str *dir* "/.gitignore")))]
    (is (= ignores (sort ignores)))))

(deftest test-files-exist
  (is (= (find-files *dir* app-name)
         [".gitignore"
          "env/dev/clj/example/repl.clj"
          "env/dev/cljs/example/boot.cljs"
          "env/prod/clj/example/system.clj"
          "env/prod/cljs/example/boot.cljs"
          "phantomjs/console-polyfill.js"
          "phantomjs/es5-sham.js"
          "phantomjs/es5-shim.js"
          "project.clj"
          "src/clj/example/components/http.clj"
          "src/clj/example/main.clj"
          "src/clj/example/server.clj"
          "src/cljs/example/client.cljs"
          "test/clj/example/server_test.clj"
          "test/clj/example/test/markup.clj"
          "test/cljs/example/client_test.cljs"])))
