(ns {{ns}}.server-test
  (:require [clojure.test :refer :all]
            [{{ns}}.server :refer :all]
            [{{ns}}.test.markup :as markup]
            [ring.mock.request :as mock]))

(def ^:private prod-js-pattern
  #"/bundles/[a-f0-9]{12}/prod\.js")

(def ^:private react-js
  "//cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react.js")

(defn- get-assets [config]
  (let [handler (make-handler config)
        {:keys [status body]} (handler (mock/request :get "/"))]
    {:status status
     :scripts (->> (markup/scripts body)
                   (map (comp :src :attrs))
                   (remove nil?))}))

(deftest test-assets-in-development
  (let [{:keys [status scripts]} (get-assets {:freeze-assets? false})]
    (is (= status 200))
    (is (= scripts ["//cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react.js"
                    "/js/out/goog/base.js"
                    "/js/app.js"]))))

(deftest test-assets-in-production
  (let [{:keys [status scripts]} (get-assets {:freeze-assets? true})]
    (is (= status 200))
    (is (= (count scripts) 1))
    (is (re-find prod-js-pattern (first scripts)))))
