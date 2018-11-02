(ns bbg.harold.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [bbg.harold.lambda-handler-node.core-test]
            [bbg.harold.parser.core-test]
            [bbg.harold.telegram.core-test]))

(doo-tests
  'bbg.harold.lambda-handler-node.core-test
  'bbg.harold.parser.core-test
  'bbg.harold.telegram.core-test)
