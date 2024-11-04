(ns retroboard.wsapi.board-topic
  (:require [retroboard.wsapi.core :as c]))

(def ^:const RETROBOARD_TOPIC "board2")

(defn pub!
  [data]
  (c/pub! RETROBOARD_TOPIC data))