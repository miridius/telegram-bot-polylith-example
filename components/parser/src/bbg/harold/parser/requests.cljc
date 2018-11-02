(ns bbg.harold.parser.requests
  (:require [clojure.string :as str]))

(def quantities
  {"some" 3 "more" 6 "LOTS_MORE" 9 "ALL_THE" 12})

(def adjectives
  {"animated" :animated "gif" :animated "video" :animated
   "better" :good "good" :good "perfect" :good "best" :good})

(defn- parse-words [sentence]
 (let [words (-> sentence
                 (str/lower-case)
                 (str/replace #"\ball the\b" "ALL_THE")
                 (str/replace #"\blots more\b" "LOTS_MORE")
                 (str/split #"\W+"))]
   (when (contains? (set words) "please")
     (loop [q 1
            adjs #{}
            nouns []
            [word & more] words]
       (cond
         (= "please" word) [q adjs nouns]
         (quantities word) (recur (quantities word) #{} [] more)
         (adjectives word) (recur q (conj adjs (adjectives word)) [] more)
         :else             (recur q adjs (conj nouns word) more))))))


(defn parse-request [text]
  (let [sentences      (str/split text #"(?<=[\.\?!])\s+")
        [q adjs nouns] (some parse-words sentences)]
    (when q
      {:text (str "You asked for " (str/join " " (concat [q] adjs nouns)))})))

(defn get-source [message_id]
  {:text (str "Sorry, I don't have a source for that message")})