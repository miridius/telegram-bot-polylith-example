(ns bbg.harold.telegram.interface
  (:require [bbg.harold.telegram.core :as core]))

(defn handle-update [update]
  (core/handle-update update))
