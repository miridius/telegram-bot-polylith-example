(ns bbg.harold.lambda-handler-jvm.core-test
  (:require [clojure.test :refer :all]
            [bbg.harold.lambda-handler-jvm.core :refer :all]
            [cheshire.core :as cheshire]))

(def test-request
  {:body (cheshire/generate-string {:x 1})})

(deftest wrap-api-gw-test
  (testing "Identity handler echoes the body"
    (let [result ((wrap-api-gw identity) test-request)]
      (is (= 200 (:statusCode result)))
      (is (= {:x 1} (cheshire/parse-string (:body result) true)))))

  (testing "JSON keys are turned into keywords"
    (let [handler (fn [req] (is (= {:x 1} req)))]
      ((wrap-api-gw handler) test-request)))

  (testing "When handler returns nil, it doesn't include a body"
    (let [handler (constantly nil)
          result ((wrap-api-gw handler) test-request)]
      (is (= 200 (:statusCode result)))
      (is (not (contains? result :body))))))
