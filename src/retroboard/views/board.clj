(ns retroboard.views.board
  (:require [retroboard.ui.components.board :refer [retroboard add-card-input add-card!]]
            [retroboard.ui.core :refer [page]]
            [hiccup2.core :as h]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.response :as r]))

(defn get-board
  [request storage & {:keys [board-key]}]
  (let [board-key (or (-> request :params :board-key) board-key)]
    (-> @storage
        board-key
        retroboard
        page
        h/html
        str
        r/response)))

(defn get-add-card-input-button
  [request]
  (-> request :params keywordize-keys add-card-input h/html str r/response))

(defn do-add-card!
  [request storage & {:keys [board-key]}]
  (-> request
      :params
      keywordize-keys
      (add-card! storage :bkey board-key)
      h/html
      str
      r/response))

