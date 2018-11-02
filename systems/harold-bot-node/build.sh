#!/usr/bin/env bash
set -e

lein cljs-lambda build :output target/harold-bot-node-standalone.zip
