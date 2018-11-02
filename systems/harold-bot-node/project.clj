(defproject bbg.harold/harold-bot-node "0.1.0-SNAPSHOT"
  :description "A harold-bot-node system."
  :url         "https://t.me/bbg_harold_bot"
  :dependencies [[com.taoensso/timbre "4.10.0"]
                 [io.nervous/cljs-lambda "0.3.5"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]]
  :plugins [[io.nervous/lein-cljs-lambda "0.6.6"]]
  :source-paths ["src"]

  :cljs-lambda
  {:functions
   [{:name "harold-bot-node-dev-update-handler"
     :invoke bbg.harold.lambda-handler-node.core/update-handler}]
   :compiler
   {:inputs  ["src" "resources"]
    :options {:output-to "target/harold-bot-node/harold_bot_node.js"
              :output-dir "target/harold-bot-node"
              :target :nodejs
              :language-in :ecmascript5
              :optimizations :simple}}})
