(ns retroboard.views.board
  (:require
   [clojure.walk :refer [keywordize-keys]]
   [hiccup2.core :as h]
   [retroboard.storage.remove :as rm]
   [retroboard.ui.components.board :as bui]
   [retroboard.ui.core :refer [page]]
   [retroboard.utils.common :as u]
   [retroboard.wsapi.board-topic :as bt]
   [ring.util.response :as r]))

(defn get-board
  [request storage & {:keys [board-key]}]
  (let [board-key (or (-> request :params :board-key) board-key)]
    (-> @storage
        board-key
        bui/retroboard
        page
        h/html
        str
        r/response)))

(defn get-add-card-input-button
  [request]
  (-> request :params keywordize-keys bui/add-card-input h/html str r/response))

(defn do-add-card!
  [request storage & {:keys [board-key]}]
  (-> request
      :params
      keywordize-keys
      (bui/add-card! storage :bkey board-key)
      h/html
      str
      r/response))

(defn do-delete-card!
  [request storage & {:keys [board-key]}]
  (let [{:keys [card-number col-number card-id]} (-> request :params keywordize-keys)]
    (swap! storage #(rm/remove-from-board-in
                     %
                     [board-key :cols (u/parse-int col-number) :cards]
                     (u/parse-int card-number)))

    (bt/pub! (-> [:div {:id card-id :hx-swap-oob "true"}]
                 h/html
                 str))
    (r/response "")))

(comment

  ;; TODO: функция добавить колонку в доску. Добавить views
  (require '[retroboard.storage.add :as a])

  (a/add-in-place @board-storage [:board-1 :cols] {:c "col-add-card" :cards [{:card "new-card-new-col"}]} :idx 6)
  (a/add-in-place @board-storage [:board-1 :cols] {:new-col "new-col" :cards []} :idx 0)
  ;; (swap! b #(a/add-in-place % [:board-1 :cols] {:new-col "new-col" :cards []} :idx 0))
  ;; (swap! b #(a/add-in-place % [:board-1 :cols 0 :cards] {:card-id 888} :idx 0))

  (defn add-column
    []
    (a/add-in-place @board-storage [:board-1 :cols] (col)))


  (defn col
    []
    {:id 4,
     :name "Column theme",
     :cards
     [{:id "CImKoAcwhT", :text "card col 1"}
      {:id "fqwer1234", :text "card col 2"}
      {:id "asdfqwerq", :text "card col 3"}]})

  (add-column)
  add-board ;; также пример
  {:board-1
   {:name "Board 1",
    :theme "theme",
    :id 1,
    :cols
    [{:id 1,
      :name "Column theme",
      :cards [{:id "CImKoAcwhT", :text "card col 1"}]}
     {:id 2,
      :name "Column theme 2",
      :cards [{:id "sSEQIxJgrY", :text "card col 2"}]}
     {:id 3,
      :name "Column theme 3",
      :cards [{:id "vMJlYNcdrN", :text "card col 3"}]}]}}

  :rcf)

(defn do-add-column!
  [request storage & {:keys [board-key]}])



(comment
  :rcf)
