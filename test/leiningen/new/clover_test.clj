(ns leiningen.new.clover-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [leiningen.new.clover :refer :all]
            [leiningen.new.templates :refer [*dir*]]
            [me.raynes.fs :refer [temp-dir]]))

(defn generate-project [test-fn]
  (let [name "example"
        sandbox (temp-dir "clover-")]
    (binding [*dir* (str sandbox "/" name)]
      (clover name)
      (try
        (test-fn)
        (finally
          (when (.isDirectory sandbox)
            (.delete sandbox)))))))

(use-fixtures :once generate-project)

(deftest test-gitignore-sorted
  (let [ignores (str/split-lines (slurp (str *dir* "/.gitignore")))]
    (is (= ignores (sort ignores)))))
