(ns retroboard.storage.boards)

(defn init-board-storage []
  (let [storage (atom {})
        board {:board-1
               {:board-theme "theme"
                :cols
                []}}]
    (swap! storage into board)
    storage))

(defn board-storage-with-defaults []
  (let [storage (atom {})
        board {:board-1
               {:board-theme "theme"
                :cols
                [{:col-name "col one"
                  :col-id 1
                  :cards [{:card-id 1
                           :card-text "card text onecol"}]}
                 {:col-name "col two"
                  :col-id 2
                  :cards [{:card-id 2
                           :card-text "card text twocol"}]}
                 {:col-name "col one"
                  :col-id 3
                  :cards [{:card-id 3
                           :card-text "card text threecol"}]}]}}]
    (swap! storage into board)
    storage))

(defn yet-another-storage
  []
  [{:board/id 1
    :col/id 1}])

(comment
  {:col-name "col name"
   :cards [{:card-id 1
            :card-text "card text"}]}
  :rcf)

(comment
  (def boards
    [{:board-id 1
      :board-theme "theme"
      :cols
      [{:col-name "col name"
        :cards [{:card-id 1
                 :card-text "card text"}]}]}])
  :rcf)