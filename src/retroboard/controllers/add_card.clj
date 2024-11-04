(ns retroboard.controllers.add-card
  (:require [hiccup2.core :as h]
            [retroboard.storage.add :as a]
            [retroboard.storage.boards :refer [new-card]]
            [retroboard.utils.common :as u]))

(defn add-card!
  "Отправить карту через websocket"
  [{:keys [col-number text-input board-key]} board & {:keys [bkey]}]

  (let [col-number (-> col-number u/parse-int)
        card (new-card :text text-input)
        board-key (or board-key bkey default-board-key)
        new-card-idx (count (get-in @board [board-key :cols col-number :cards]))]

    (swap! board #(a/add-in-place % [board-key :cols col-number :cards] card))
    (pub! (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
               (board-card new-card-idx {:text text-input})]
              h/html
              str))

    (get-add-card-input-button col-number)))
