(ns bbg.harold.lambda-handler-node.core
  (:require [bbg.harold.telegram.interface :as telegram]
            [cljs-lambda.macros :refer-macros [defgateway]]
            [taoensso.timbre :as log]))

(defn- get-payload [api-gw-request]
  (-> (.parse js/JSON (:body api-gw-request))
      (js->clj :keywordize-keys true)))

(defn wrap-api-gw [handler-fn]
  (fn wrapped-handler [request]
    (try
      (let [payload (log/spy :info (get-payload request))
            result (log/spy :info (handler-fn payload))]
        (if result
          {:status  200
           :headers {:content-type (-> request :headers :content-type)}
           :body    (.stringify js/JSON (clj->js result))}
          {:status 200}))
      (catch :default e
        (log/error e)
        {:status 500
         :body   (str e)}))))

(def handler (-> telegram/handle-update
                 wrap-api-gw))

(defgateway update-handler [request _]
  (log/spy :info (handler (log/spy :info request))))
