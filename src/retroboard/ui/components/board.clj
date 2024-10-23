(ns retroboard.ui.components.board
  (:require [hiccup2.core :as h]
            [retroboard.wsapi.core :refer [boards]]
            [manifold.bus :as bus]))

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

;; TODO:
;; Получается, надо было сразу располагать все компоненты в одном месте, будто это
;; веб компонент. Там, где html, там и код, и общие переменные. Объявить внутренние
;; константы и использовать их. Мб функции генерации id'шников

"Что дальше
 Нужно из инпута отправлять текст, чтобы добавить в конкретную колонку
 Но наверное сначала лучше добавить две колонки, чтобы было удобнее тестировать
 колонки добавли. Добавляем текст
 
 "
(defn add-card-input
  "Поле ввода для добавления новой карты в колонку"
  [request]
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

(defn bboard
  "Основная доска"
  [& {:keys [board-name] :or {board-name default-board}}]
  [:div {:id "retroboard" :class "container-fluid"}
   [:input {:type "hidden" :name (::board n) :value board-name}]
   [:div {:id "board-theme-text" :class "card-body"}
    [:h5 {:id "board-theme-title" :class "card-title"}
     "Основная тема"]
    [:p {:id "board-theme-desc" :class "card-text"}
     "Описание темы"]]
     ;; TODO: row-cols-#
   [:div {:id "retroboard-cards"
          :class "row row-cols-4"
          :hx-ext "ws"
          ;; TODO: ws-connect порт должен быть такой же как у основного приложения
          :ws-connect "ws://localhost:5000/ws/card-operations"}
    ;; TODO: здесь скорее всего нужно генерировать карты на основе входных параметров
    ;; Колонка 1
    [:div {:id "row" :class "flex-fill row"}
     [:input {:type "hidden" :name (::col-number n) :value "1"}]
    ;; Элемент: карты в колонке
     [:div
      {:id "col-1"}
      [:div {:id "card-1"} "card data 1"]
      [:div {:id "card-2"} "card data 2"]
      [:div {:id "card-3"} "card data 3"]
      [:div {:id "card-4"} "card data 4"]]
     (get-add-card-input-button)]

    ;; Колонка 2
    [:div {:id "row" :class "flex-fill row"}
     [:input {:type "hidden" :name (::col-number n) :value "2"}]
    ;; Элемент: карты в колонке
     [:div
      {:id "col-2"}
      [:div {:id "card-1"} "card data 1"]
      [:div {:id "card-2"} "card data 2"]
      [:div {:id "card-3"} "card data 3"]
      [:div {:id "card-4"} "card data 4"]]
     (get-add-card-input-button)]]])

;; TODO: принимать название канала в boards
(defn pub!
  ([text] (pub! default-board text))
  ([board text] (bus/publish! boards board text)))

(defn do-add-card
  "Отправить карту через websocket"
  [{:keys [board col-number text-input] :as params}]
  (println "do-add-card params: " params)
  (pub! (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
             [:div text-input]]
            h/html
            str))
  [:div])