(ns retroboard.storage.common)

(defn in-place
  [arr el idx]
  (if (<= idx (count arr))
    (let [before (subvec arr 0 idx)
          after (subvec arr idx)]
      (vec (concat before [el] after)))
    arr))