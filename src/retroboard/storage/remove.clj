(ns retroboard.storage.remove)

(defn remove-by-idx
  [v index]
  (if (< index (count v))
    (vec (concat (subvec v 0 index) (subvec v (inc index))))
    v))

(defn remove-from-board-in
  [storage path idx]
  (update-in storage path remove-by-idx idx))
