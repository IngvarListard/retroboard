(ns retroboard.ui.components.board
  (:require [hiccup2.core :as h]
            [retroboard.wsapi.core :refer [boards]]
            [manifold.bus :as bus]
            [retroboard.storage.add :as a]
            [retroboard.storage.boards :refer [new-card]]
            [retroboard.utils.common :as u]))

(def f
  "Сквозные параметры компонента"
  {:text-input "text-input"})

(def i
  "Сквозные идентификаторы компонента"
  {:text-input "text-input"})

(def n
  "Сквозные имена компонентов"
  {::col-number "col-number"
   ::board "board"})

(def k
  "Сквозные ключи атрибутов компонентов"
  {::col-number :col-number})

(defn add-card-input
  "Поле ввода для добавления новой карты в колонку"
  [request]
  (println "request add card input: " (:params request))
  [:div
   [:div
    {:class "mb-3"}
    [:div
     [:input
      {:type "text",
       :class "form-control",
       :placeholder "Введите текст..."
       :name "text-input"
       :id (:text-input i)}]
     [:div {:class "d-grid justify-content-end"}
      [:button {:class "btn btn-outline-secondary"
                :type "button"
                :hx-include "closest #row"
                :hx-post "/api/board/add-card-input"}

       [:img {:class "icon" :src "/icons/check2.svg"}]]]]]])

(defn get-add-card-input-button
  "Кнопка для получения формы ввода для добавления новой карты в колонку"
  []
  [:div
   [:button
    {:class "btn btn-outline-secondary"
     :hx-include "closest #row"
     :hx-get "/api/board/add-card-input"
     :hx-swap "outerHTML"}
    [:img {:class "icon" :src "/icons/plus-lg.svg"}]]])

(def default-board "board2")

(defn board-card
  [{:keys [id text]}]
  [:div
   {:id id :class "card"}
   [:div text]])

(defn board-column
  [idx {:keys [id name cards] :as params}]
  [:div {:id "row" :class "flex-fill row"}
   [:input {:type "hidden" :name (::col-number n) :value idx}]
   [:p name]
    ;; Элемент: карты в колонке
   (into
    [:div]
    (vec (map board-card cards)))
   (get-add-card-input-button)])

(defn bboard
  "Основная доска"
  [storage]
  (let [board-name (get-in @storage [:board-1 :name])]
    [:div {:id "retroboard" :class "container-fluid"}
     [:input {:type "hidden" :name (::board n) :value board-name}]
     [:div {:id "board-theme-text" :class "card-body"}
      [:h5 {:id "board-theme-title" :class "card-title"}
       board-name]
      [:p {:id "board-theme-desc" :class "card-text"}
       (get-in @storage [:board-1 :theme])]]
     ;; TODO: row-cols-#
     (into
      [:div {:id "retroboard-cards"
             :class "row row-cols-4"
             :hx-ext "ws"
          ;; TODO: ws-connect порт должен быть такой же как у основного приложения
             :ws-connect "ws://localhost:5000/ws/card-operations"}]

      (vec (map-indexed board-column (get-in @storage [:board-1 :cols]))))]))

(comment
  (def asdf {:id 1, :name "Column theme", :cards [{:id "e4e84d21-be16-4f0a-87d7-57130fda13c2", :text "card col 1"}]})
  (into
   [:div {:id "row" :class "flex-fill row"}]
   (vec (map-indexed board-column [asdf])))
  :rcf)
;; TODO: принимать название канала в boards

(defn pub!
  ([text] (pub! default-board text))
  ([board text] (bus/publish! boards board text)))

(defn do-add-card
  "Отправить карту через websocket"
  [{:keys [col-number text-input] :as params} board]
  (let [col-number (-> col-number u/parse-int)]
    (println "do-add-card params: " params)
    (swap! board #(a/add-in-place % [:board-1 :cols col-number :cards] (new-card :text text-input)))
    (pub! (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
               [:div text-input]]
              h/html
              str)))

  [:div])