(ns bbg.harold.telegram.core-test
  (:require [clojure.test :refer [deftest testing is are]]
            [bbg.harold.parser.interface :as parser]
            [bbg.harold.telegram.interface :as telegram]))

(deftest handle-update-test
  (testing "It parses the text you sent it"
    (let [update {:message {:text "👊" :chat {:id 1}}}
          result (telegram/handle-update update)]
      (is (= "👊" (:text result)))
      (is (= 1 (:chat_id result)))))

  (testing "It responds to harold-01 with the same sticker"
    (let [sticker (parser/get-sticker-id :harold-01)
          update  {:message {:sticker {:file_id sticker} :chat {:id 1}}}
          result  (telegram/handle-update update)]
      (is (= sticker (:sticker result)))
      (is (= 1 (:chat_id result)))))

  (testing "It ignores other stickers"
    (let [update {:message {:sticker {:file_id "xyz"} :chat {:id 1}}}
          result (telegram/handle-update update)]
      (is (nil? result))))

  (testing "It ignores other message types or empty updates"
    (is (nil? (telegram/handle-update {:message {:photo "abc"}})))
    (is (nil? (telegram/handle-update nil)))))
