(ns retroboard.storage.boards
  (:require
   [retroboard.utils.common :as utils])
  (:import
   [java.util UUID]))

(defn gen-uuid
  []
  (str (UUID/randomUUID)))

(defn new-col
  [& {:keys [id name cards]
      :or {id (gen-uuid) name "NO COL THEME" cards []}}]
  {:id id :name name :cards cards})

(defn new-card
  [& {:keys [id text]
      :or {id (utils/random-string) text ""}}]
  {:id id :text text})

(defn add-board
  []
  (let [storage (atom {})
        board {:board-1
               {:name "Board 1"
                :theme "theme"
                :id 1
                :cols
                [{:id 1
                  :name "Column theme"
                  :cards [(new-card :text "card col 1")]}
                 {:id 2
                  :name "Column theme 2"
                  :cards [(new-card :text "card col 2")]}
                 {:id 3
                  :name "Column theme 3"
                  :cards [(new-card :text "card col 3")]}]}}]
    (swap! storage into board)
    storage))
