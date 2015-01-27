(ns {{ns}}.client-test
  (:require-macros [cemerick.cljs.test :refer [deftest
                                               is
                                               run-tests
                                               test-var
                                               testing
                                               with-test]])
  (:require [cemerick.cljs.test :as t]))

(deftest test-me-later
  (is (= (+ 1 1) 2)))
