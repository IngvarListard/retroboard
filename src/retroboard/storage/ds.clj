(ns retroboard.storage.ds
  (:require [datascript.core :as d]))


(def schema {:board/theme {:db/unique :db.unique/identity}
             :col/name   {:db/unique :db.unique/identity}
             :col/id     {:db/unique :db.unique/identity}
             :card/id    {:db/unique :db.unique/identity}
             :card/text  {}})

(def conn (d/create-conn schema))

(d/transact! conn [{:db/id -1
                    :board/theme "theme"
                    :col/name "col one"
                    :col/id 1
                    :card/id 1
                    :card/text "card text onecol"}
                   {:db/id -2
                    :board/theme "theme"
                    :col/name "col two"
                    :col/id 2
                    :card/id 2
                    :card/text "card text twocol"}
                   {:db/id -3
                    :board/theme "theme"
                    :col/name "col three"
                    :col/id 3
                    :card/id 3
                    :card/text "card text threecol"}])


(defn add-card [conn col-id new-card]
  (let [col (d/pull conn '[*] [:col/id col-id])]
    (when col
      (d/transact! conn [{:db/id -1
                          :card/id (:card-id new-card)
                          :card/text (:card-text new-card)
                          :col/id col-id}]))))


(defn remove-card [conn card-id]
  (d/transact! conn [[:db.fn/retractEntity card-id]]))

(comment
  (defn add-records [conn]
    (let [tx-data [{:board/theme "My Board2"}
                   {:col/name "Column 1" :col/id 3}
                   {:col/name "Column 2" :col/id 4}
                   {:card/id 8 :card/text "This is a card in Column 1"}
                   {:card/id 9 :card/text "This is a card in Column 2"}]]
      (d/transact! conn tx-data)))

;; Вызов функции для добавления записей
  (def updated-conn (add-records conn))
  updated-conn
  :rcf)

(comment
 ;; Добавление карточки
  (def new-card {:card-id 4 :card-text "card text fourcol"})
  (add-card @conn 101 new-card)

;; Удаление карточки
  (remove-card conn 3) ; Удаляем карточку с card-id 2

  (d/q '[:find ?col-name ?card-text
         :where
         [?c :col/name ?col-name]
         [?c :col/id ?col-id]
         [?card :card/id ?card-id]
         [?card :card/text ?card-text]
         [?c :col/id ?col-id]]
       @conn)

  :rcf)