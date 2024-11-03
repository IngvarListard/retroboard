(ns retroboard.storage.add
  (:require [retroboard.storage.common :as c]))

(defn add-in-place
  "Вставка значения в массив"
  [storage path new-val & {:keys [idx]}]
  (let [arr (get-in storage path)
        idx (or idx (count arr))
        -add-col (fn [cols-arr col]
                   (c/in-place cols-arr col idx))]
    (update-in storage path -add-col new-val)))

