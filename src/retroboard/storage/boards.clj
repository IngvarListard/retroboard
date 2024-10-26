(ns retroboard.storage.boards)

(defn board
  []
  (let [storage (atom {})
        board {:board-1
               {:board-theme "theme"
                :board-id 1
                :cols
                []}}]
    (swap! storage into board)
    storage))


(comment (in-place [] "element" 1))
(defn in-place
  [arr el idx]
  (if (<= idx (count arr))
    (let [before (subvec arr 0 idx)
          after (subvec arr idx)]
      (vec (concat before [el] after)))
    []))

(defn insert-col-by-idx
  [storage board col col-idx]
  #_(update-in storage [board :cols] #(in-place %1 %2 place-number) col)
  (let [-add-col (fn [cols-arr col]
                   #_(println a b place-number)
                   (in-place cols-arr col col-idx))]
    (update-in storage [board :cols] -add-col col)))

(defn remove-by-idx
  [v index]
  (if (< index (count v))
    (vec (concat (subvec v 0 index) (subvec v (inc index))))
    v))

(defn remove-col-by-idx
  [storage board col-idx]
  (update-in storage [board :cols] remove-by-idx col-idx))

(defn update-col-by-idx
  [storage board upd-col col-idx]
  (let [path [board :cols col-idx]]
    (if (< col-idx (count (get-in storage [board :cols])))
      (update-in storage path #(merge %1 %2) upd-col)
      storage)))

(comment
  (def b (board))

  ;; (add-col @b :board-1 {:c "col3"} 2)
  ;; (remove-col-in-place @b :board-1 1)
  (update-col-by-idx @b :board-1 {:c "new-val"} 3)

  (swap! b #(insert-col-by-idx % :board-1 {:c "col000"} 0))
  (swap! b #(remove-col-by-idx % :board-1 0))
  (swap! b #(update-col-by-idx % :board-1 {:c "new-val" :b "fqwerqwe"} 2))

  (def bb {:board-1
           {:board-theme "theme"
            :board-id 1
            :cols
            [{:c "col1"} {:c "col2"}]}})

  (let [before (subvec (get-in bb [:board-1 :cols]) 0 1)
        after (subvec (get-in bb [:board-1 :cols]) 1)]
    (vec (concat before [{"col11" "col11"}] after)))


  (update-in bb [:board-1 :cols] conj {"abrbal" "abrbal"})
  (swap! b #(update-in % [:board-1 :cols] conj {"abrbal" "abrbal"}))
  (clojure.pprint/pprint @b)
  :rcf)