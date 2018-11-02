(ns bbg.harold.parser.core
  (:require [bbg.harold.parser.requests :as requests]
            [clojure.string :as str]
            [taoensso.timbre :as log]))

(def get-sticker-id
  {:harold-01 "BQADBAADXAADXSupATtI_RyIWnrvAg"    ; hide the pain face
   :harold-02 "BQADBAADXgADXSupAYuaA7Qvv6W7Ag"    ; thumbs up sweater
   :harold-03 "BQADBAADYAADXSupAWiaKbRZL688Ag"    ; thumbs up white shirt
   :harold-04 "BQADBAADYgADXSupAV3ox7tvPavPAg"    ; thumbs up blue shirt
   :harold-05 "BQADBAADZAADXSupAUi7eFfA_S3vAg"    ; casual lean
   :harold-06 "BQADBAADZgADXSupARwvmj-WrFgeAg"    ; are you srs
   :harold-07 "BQADBAADaAADXSupAfO3UNKx_UN-Ag"    ; gamer harold
   :harold-08 "BQADBAADagADXSupAQsbG7vJr-NUAg"    ; excited face
   :harold-09 "BQADBAADbAADXSupASiubI4Zpl2-Ag"    ; excited arms
   :harold-10 "BQADBAADbgADXSupAdl-C0qDP0eNAg"})  ; migrane

(def my-ids
  #{699432634})

(defn- send-sticker
  "When called with more than one arg, chooses a sticker at random"
  [& stickers]
  {:sticker (get-sticker-id (rand-nth stickers))})

(defn- includes? [phrase text]
  (or (= phrase text)
      (re-seq (re-pattern (str "\\b" phrase "\\b")) text)))

(defn parse-sticker-msg [sticker]
  (cond
    (= (get-sticker-id :harold-01) sticker)
    {:sticker sticker}

    (contains? (into #{} (vals get-sticker-id)) sticker)
    (send-sticker (keys get-sticker-id))))

(defn parse-text-message [text]
  (condp includes? (-> text str/lower-case str/trim)
    "thanks.? harold" (send-sticker :harold-02 :harold-03 :harold-04)
    "good (job|work),? harold" (send-sticker :harold-05 :harold-08)
    "you rock,? harold" (send-sticker :harold-09)
    "cheers.? harold" (send-sticker :harold-01)
    "👊" {:text "👊"}
    (requests/parse-request text)))

(defn parse-reply [text {:keys [message_id from]}]
  (when (my-ids (:id from))
    (if (some #(includes? % text) ["source" "info" "link" "where"])
      (requests/get-source message_id)
      (send-sticker :harold-05))))

(defn parse-message [{:keys [text sticker reply_to_message] :as message}]
  (cond reply_to_message (parse-reply text reply_to_message)
        text    (parse-text-message text)
        sticker (parse-sticker-msg (:file_id sticker))))
