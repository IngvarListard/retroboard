(ns retroboard.storage.update)

(defmulti update-board-in (fn [storage path _]
                              (println (type (get-in storage (conj path))))
                              (type (get-in storage (conj path)))))

(defmethod update-board-in
  clojure.lang.PersistentArrayMap
  [storage path new-val]

  (println "Обработка мапы")
  ;; Здесь можно добавить логику обработки мапы
  (if (< (last path) (count (get-in storage path)))
    (do
      (println "Полученное значение по индексу" (get-in storage path))
      (update-in storage path #(merge %1 %2) new-val))
    storage))

;; Определяем метод для обработки вектора
(defmethod update-board-in
  clojure.lang.PersistentVector
  [storage path new-val]

  (println "Значыение которое будет обновлено" (get-in storage path))
  (println "Значение" new-val)
  (assoc-in storage path new-val))

(defmethod update-board-in
  java.lang.String
  [& _]
  (println "Обработка строки"))

(defmethod update-board-in
  :default
  [storage _ _] storage)
