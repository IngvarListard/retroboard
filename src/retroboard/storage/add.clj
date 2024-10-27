(ns retroboard.storage.add
  (:require [retroboard.storage.common :as c]))

(defn add-in-place
  "Вставка значения в массив"
  [storage path new-val idx]
  (let [arr (get-in storage path)
        -add-col (fn [cols-arr col]
                   (println "Полученный массив: " arr)
                   (c/in-place cols-arr col idx))]
    (update-in storage path -add-col new-val)))

