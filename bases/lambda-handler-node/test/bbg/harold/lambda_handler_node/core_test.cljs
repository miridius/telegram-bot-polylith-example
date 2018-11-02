(ns bbg.harold.lambda-handler-node.core-test
  (:require [cljs.test :refer-macros [deftest testing is are]]
            [bbg.harold.lambda-handler-node.core :as core]))

(def test-request
  {:body (.stringify js/JSON (clj->js {:x 1}))})

(deftest wrap-api-gw-test
  (testing "Identity handler echoes the body"
    (let [result ((core/wrap-api-gw identity) test-request)]
      (is (= 200 (:status result)))
      (is (= {:x 1} (-> (.parse js/JSON (:body result))
                        (js->clj :keywordize-keys true))))))

  (testing "JSON keys are turned into keywords"
    (let [handler (fn [req] (is (= {:x 1} req)))]
      ((core/wrap-api-gw handler) test-request)))

  (testing "When handler returns nil, it doesn't include a body"
    (let [handler (constantly nil)
          result ((core/wrap-api-gw handler) test-request)]
      (is (= 200 (:status result)))
      (is (not (contains? result :body))))))
