(ns bbg.harold.parser.core-test
  (:require [clojure.test :refer [deftest testing is are]]
            [bbg.harold.parser.interface :as parser]))

(def message-with-reply
  {:message_id 63
   :from {:id 60764253
          :is_bot false
          :first_name "Dave"
          :last_name "Rolle"
          :username "DavidRolle"
          :language_code "en-AU"}
   :chat {:id 60764253
          :first_name "Dave"
          :last_name "Rolle"
          :username "DavidRolle"
          :type "private"}
   :date 1539632916
   :reply_to_message {:message_id 61
                      :from {:id 699432634
                             :is_bot true
                             :first_name "Harold"
                             :username "harold_dev_bot"}
                      :chat {:id 60764253
                             :first_name "Dave"
                             :last_name "Rolle"
                             :username "DavidRolle"
                             :type "private"}
                      :date 1539632213
                      :sticker {:width 512
                                :height 512
                                :emoji "✋"
                                :set_name "HideThePainHarold"
                                :thumb {:file_id "AAQEABOdi4QrAAS6RNb9CzlM9tvUAAIC"
                                        :file_size 5038
                                        :width 128
                                        :height 128}
                                :file_id "CAADBAADZAADXSupAUi7eFfA_S3vAg"
                                :file_size 43550}}
   :text "cool"})

(defn sticker-msg [sticker-id]
  {:sticker {:file_id sticker-id}})

(deftest test-parse-sticker-message
  (testing "It replies to harold-01 with harold-01"
    (let [sticker (:harold-01 parser/get-sticker-id)
          reply   (parser/parse-message (sticker-msg sticker))]
      (is (= {:sticker sticker} reply))))

  (testing "It ignores other stickers"
    (let [sticker "abc"
          reply   (parser/parse-message (sticker-msg sticker))]
      (is (nil? reply)))))

(deftest test-parse-text-message
  (testing "It matches phrases in the input text and responds with stickers"
    (are [text stickers]
      (let [result (parser/parse-message {:text text})
            sticker-ids (into #{} (map parser/get-sticker-id stickers))]
        (is (sticker-ids (:sticker result))))
      "Thanks harold!" [:harold-02 :harold-03 :harold-04]
      "wow good job harold" [:harold-05 :harold-08]
      "good work, harold :)" [:harold-05 :harold-08]
      "You rock Harold. Lol" [:harold-09]))

  (testing "It replies to fist pounds"
    (is (= {:text "👊"} (parser/parse-message {:text "👊"}))))

  (testing "It ignores other messages"
    (is (nil? (parser/parse-message {:text "omgwtfbbq"})))))

(deftest test-parse-reply
  (testing "It replies to requests for source"
    (let [msg (assoc message-with-reply :text "can I get a source plz")
          result (parser/parse-message msg)]
      (is (string? (:text result)))))

  (testing "It sends a sticker to random replies"
    (let [result (parser/parse-message message-with-reply)]
      (is (string? (:sticker result)))))

  (testing "It ignores replies to other users"
    (let [msg (assoc-in message-with-reply [:reply_to_message :from :id] 1)
          result (parser/parse-message msg)]
      (is (nil? result)))))

(deftest test-parse-request
  (testing "It understands what you want"
    (are [text things]
      (let [reply (:text (parser/parse-message {:text text}))]
        (is (= (str "You asked for " things) reply))
        true)
      "Lol. kittens please! Thanks man" "1 kittens"
      "more good things please" "6 :good things"
      "lots more things please" "9 things"
      "some bouncy elephants please" "3 :animated elephants"))

  (testing "It treats everything as lowercase"
    (are [text things]
      (let [reply (:text (parser/parse-message {:text text}))]
        (is (= (str "You asked for " things) reply))
        true)
      "Dog please" "1 dog"
      "LOTS MoRe BeSt ThInGs pLeAsE" "9 :good things")))
