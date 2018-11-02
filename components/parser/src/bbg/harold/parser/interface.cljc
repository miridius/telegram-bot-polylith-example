(ns bbg.harold.parser.interface
  (:require [bbg.harold.parser.core :as core]))

(def get-sticker-id
  "A lookup map of commonly used sticker IDs"
  core/get-sticker-id)

(defn parse-message
  "Reads a telegram message and interprets what the user wants"
  [message]
  (core/parse-message message))