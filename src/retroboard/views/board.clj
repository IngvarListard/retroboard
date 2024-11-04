(ns retroboard.views.board
  (:require [retroboard.ui.components.board :refer [retroboard add-card-input add-card!]]
            [retroboard.ui.core :refer [page]]
            [hiccup2.core :as h]
            [clojure.walk :refer [keywordize-keys]]
            [ring.util.response :as r]
            [retroboard.storage.remove :as rm]
            [retroboard.wsapi.core :refer [board-transport pub!]]
            [retroboard.utils.common :as u]
            [retroboard.wsapi.board-topic :as bt]
            [cheshire.core :as json]))

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

(defn do-delete-card!
  [request storage & {:keys [board-key]}]
  (println "params" (:params request))
  (let [{:keys [card-number col-number card-id]} (-> request :params keywordize-keys)]
    (println "delete params 2" card-number col-number)
    (swap! storage #(rm/remove-from-board-in % [board-key :cols (u/parse-int col-number) :cards] (u/parse-int card-number)))

    (bt/pub! (-> [:div {:id card-id
                        :hx-swap-oob "true"}]
                 h/html
                 str))
    (r/response "")))
    

(comment
  (defn add-card!
    "Отправить карту через websocket"
    [{:keys [col-number text-input board-key]} board & {:keys [bkey]}]

    (let [col-number (-> col-number u/parse-int)
          card (new-card :text text-input)
          board-key (or board-key bkey default-board-key)
          new-card-idx (count (get-in @board [board-key :cols col-number :cards]))]

      (swap! board #(a/add-in-place % [board-key :cols col-number :cards] card))
      (pub! retroboard-topic
            (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
                 (board-card new-card-idx {:text text-input})]
                h/html
                str))

      (get-add-card-input-button col-number)))
  :rcf)
