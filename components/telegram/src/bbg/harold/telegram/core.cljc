(ns bbg.harold.telegram.core
  (:require [bbg.harold.parser.interface :as parser]
            [taoensso.timbre :as log]))

(defn- api-response [update {:keys [text sticker] :as reply}]
  (merge {:method  (cond text    "sendMessage"
                         sticker "sendSticker")
          :chat_id (get-in update [:message :chat :id])}
         reply))

(defn handle-update [update]
  (if-let [message (or (:message update) (:edited-message update))]
    (if-let [reply (parser/parse-message message)]
      (api-response update reply)
      (log/info "Ignored message:" message))
    (log/warn "Update did not contain a message:" update)))