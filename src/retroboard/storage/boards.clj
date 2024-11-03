(ns retroboard.storage.boards
  (:require  [retroboard.storage.update :as u]
             [retroboard.storage.common :as c]
             [retroboard.storage.add :as a]
             [retroboard.storage.remove :as r])
  (:import [java.util UUID]))

(defn gen-uuid
  []
  (str (UUID/randomUUID)))

(defn new-col
  [& {:keys [id name cards]
      :or {id (gen-uuid) name "NO COL THEME" cards []}}]
  {:id id :name name :cards cards})

(defn new-card
  [& {:keys [id text]
      :or {id (gen-uuid) text ""}}]
  {:id id :text text})

(defn new-board
  [board-key name theme & {:keys [board-id cols] :or {cols []}}]
  {board-key
   {:name name
    :theme theme
    :id board-id
    :cols cols}})

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

(comment
  (def b (add-board))

  (a/add-in-place @b [:board-1 :cols] {:c "col-add-card" :cards [{:card "new-card-new-col"}]} :idx 6)
  (a/add-in-place @b [:board-1 :cols] {:new-col "new-col" :cards []} :idx 0)
  (swap! b #(a/add-in-place % [:board-1 :cols] {:new-col "new-col" :cards []} :idx 0))
  (swap! b #(a/add-in-place % [:board-1 :cols 0 :cards] {:card-id 888} :idx 0))

  (r/remove-from-board-in @b [:board-1 :cols] 0)
  (swap! b #(r/remove-from-board-in % [:board-1 :cols 0 :cards] 0))

  (u/update-board-in
   @b
   [:board-1 :cols 1]
   {:cards [{:new-brave-card "brave card"}] :not-card "another-val-2"}) ;; Обработка вектора

  ;; обновление карты
  (u/update-board-in
   @b
   [:board-1 :cols 0 :cards 0 :card]
   {:abrbal "abrbal"})

  (u/update-board-in
   @b
   [:board-1 :cols 0 :cards 0]
   {:card-id "easdf"})

  (u/update-board-in @b [:board-1 :cols] 0 {:not-card "another-val-2"})


  :rcf)

(comment
  :draft

  (defn insert-col-by-idx
    [storage board col col-idx]
    (let [-add-col (fn [cols-arr col] (c/in-place cols-arr col col-idx))]
      (update-in storage [board :cols] -add-col col)))
  (swap! b #(insert-col-by-idx % :board-1 {:c "col000" :cards [{:card "new-val"}]} 0))

  (defn update-col-by-idx
    [storage board upd-col col-idx]
    (let [path [board :cols col-idx]]
      (if (< col-idx (count (get-in storage [board :cols])))
        (update-in storage path #(merge %1 %2) upd-col)
        storage)))

  (defn update-in-path
    [storage path idx val]

    (println (get-in storage (conj path idx)))
    (if (< idx (count (get-in storage (conj path idx))))
      (do
        (println (get-in storage (conj path idx)) val)
        (update-in storage (conj path idx) #(merge %1 %2) val))
      storage))
  '("Folded"
    (update-col-by-idx @b :board-1 {:c "new-val"} 3)
    (update-in-path @b [:board-1 :cols 2 :cards] 0 {:card "another-val-1"})
    (update-in-path @b [:board-1 :cols] 2 {:not-card "another-val-2"})
    (update-col-by-idx @b :board-1 {:c "new-val" :b "fqwerqwe"} 2)
    (swap! b #(update-col-by-idx % :board-1 {:c "new-val" :b "fqwerqwe"} 2)))
  :rcf)
