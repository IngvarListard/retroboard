(ns retroboard.views.board
  (:require
   [clojure.walk :refer [keywordize-keys]]
   [hiccup2.core :as h]
   [retroboard.storage.remove :as rm]
   [retroboard.ui.components.board :as bui]
   [retroboard.ui.core :refer [page]]
   [retroboard.utils.common :as u]
   [retroboard.wsapi.board-topic :as bt]
   [ring.util.response :as r]))

(defn get-board
  [request storage & {:keys [board-key]}]
  (let [board-key (or (-> request :params :board-key) board-key)]
    (-> @storage
        board-key
        bui/retroboard
        page
        h/html
        str
        r/response)))

(defn get-add-card-input-button
  [request]
  (-> request :params keywordize-keys bui/add-card-input h/html str r/response))

(defn do-add-card!
  [request storage & {:keys [board-key]}]
  (-> request
      :params
      keywordize-keys
      (bui/add-card! storage :bkey board-key)
      h/html
      str
      r/response))

(defn do-delete-card!
  [request storage & {:keys [board-key]}]
  (let [{:keys [card-number col-number card-id]} (-> request :params keywordize-keys)]
    (swap! storage #(rm/remove-from-board-in
                     %
                     [board-key :cols (u/parse-int col-number) :cards]
                     (u/parse-int card-number)))

    (bt/pub! (-> [:div {:id card-id :hx-swap-oob "true"}]
                 h/html
                 str))
    (r/response "")))
