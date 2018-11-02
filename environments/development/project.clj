(defproject bbg.harold/development "1.0"
  :description "The main development environment."
  :url         "https://t.me/bbg_harold_dev_bot"
  :dependencies [[cheshire "5.8.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [io.nervous/cljs-lambda "0.3.5"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [uswitch/lambada "0.1.2"]]
  :profiles
  {:dev
   {:plugins   [[com.jakemccrary/lein-test-refresh "0.23.0"]
                [lein-doo "0.1.7"]]
    :aliases   {"test" ["do" ["doo" "once"] "test"]}
    :doo       {:build "node-test"
                :alias {:default [:node]}}
    :cljsbuild {:builds
                [{:id           "node-test"
                  :source-paths ["src" "test"]
                  :compiler
                  {:output-to     "target/node-test/node_test.js"
                   :output-dir    "target/node-test"
                   :target        :nodejs
                   :language-in   :ecmascript5
                   :optimizations :none
                   :main          bbg.harold.test-runner}}]}}})
