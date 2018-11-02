(ns bbg.harold.lambda-handler-jvm.core
  (:require [bbg.harold.telegram.interface :as telegram]
            [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [taoensso.timbre :as log]
            [uswitch.lambada.core :refer [deflambdafn]]))

(defn- get-payload [api-gw-request]
  (cheshire/parse-string (:body api-gw-request) true))

(defn wrap-api-gw [handler-fn]
  (fn wrapped-handler [request]
    (try
      (let [payload (log/spy :info (get-payload request))
            result (log/spy :info (handler-fn payload))]
        (if result
          {:statusCode 200
           :headers    {"Content-Type" "application/json"}
           :body       (cheshire/generate-string result)}
          {:statusCode 200}))
      (catch Exception e
        (log/error e)
        {:statusCode 500
         :body       (.getMessage e)}))))

(def handler (-> telegram/handle-update
                 wrap-api-gw))

(deflambdafn bbg.harold.TelegramLambdaHandler [in out _]
  (let [request (log/spy :info (cheshire/parse-stream (io/reader in) true))
        result  (log/spy :info (handler request))]
    (with-open [w (io/writer out)]
      (cheshire/generate-stream result w))))
