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
  {::col-number "col-number"})

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
  (println "request params" (:params request))
  [:div
   [:div
    {:class "mb-3"}
    [:div
     [:input {:type "hidden" :name (::col-number n) :value "wtf-card-input" :id "another-param"}]
     [:input
      {:type "text",
       :class "form-control",
       :placeholder "Введите текст..."
       :name "text-input"
       :id (:text-input i)}]
     [:div {:class "d-grid justify-content-end"}
      [:button {:class "btn btn-outline-secondary"
                :type "button"
                :hx-include (str "#" (:text-input f))
                :hx-post "/api/board/add-card-input"}
                ;; :hx-include (format "#%s, #%s, #%s, #%s" room-id operation-id input-id text-input-id)

       [:img {:class "icon" :src "/icons/check2.svg"}]]]]]])

(defn get-add-card-input-button
  "Кнопка для получения формы ввода для добавления новой карты в колонку"
  []
  [:div
   [:input {:type "hidden" :name "add-card-input" :value "row-1" :id "row-1"}]
   [:button
    {:class "btn btn-outline-secondary"
     :type "button"
     :hx-include "#row-id"
     :hx-get "/api/board/add-card-input"
     :hx-swap "outerHTML"}
    [:img {:class "icon" :src "/icons/plus-lg.svg"}]]])

(defn retroboard
  "Основная доска"
  []
  [:div {:id "retroboard"
         :class "container-fluid"}
   [:div {:id "board-theme-text"
          :class "card-body"}
    [:h5 {:id "board-theme-title"
          :class "card-title"} "Основная тема"]
    [:p {:id "board-theme-desc"
         :class "card-text"}
     "Описание темы"]]
     ;; TODO: row-cols-#
   [:div {:id "retroboard-cards"
          :class "row row-cols-4"
          :hx-ext "ws"
          ;; TODO: ws-connect порт должен быть такой же как у основного приложения
          :ws-connect "ws://localhost:5000/ws/card-operations"}
    ;; TODO: здесь скорее всего нужно генерировать карты на основе входных параметров
    ;; Колонка 1
    [:div {:id "row-1" :class "flex-fill row row-cols-1" (::col-number k) "1"}
    ;; Элемент: карты в колонке
     [:div
      {:id "col-1-cards"}
      [:div {:id "card-1"} "card data 1"]
      [:div {:id "card-2"} "card data 2"]
      [:div {:id "card-3"} "card data 3"]
      [:div {:id "card-4"} "card data 4"]]
     (get-add-card-input-button)]

    ;; Колонка 2
    [:div {:id "row-1" :class "flex-fill row row-cols-1" (::col-number k) "2"}
    ;; Элемент: карты в колонке
     [:div
      {:id "col-1-cards"}
      [:div {:id "card-1"} "card data 1"]
      [:div {:id "card-2"} "card data 2"]
      [:div {:id "card-3"} "card data 3"]
      [:div {:id "card-4"} "card data 4"]]
     (get-add-card-input-button)]]])

;; TODO: принимать название канала в boards
(defn pub [text] (bus/publish! boards "board2" text))

(defn do-add-card
  "Отправить карту через websocket"
  [{:keys [text-input] :as params}]
  (println "do-add-card params: " params)
  (pub (-> [:div {:hx-swap-oob "beforeend:#col-1-cards"}
            [:div text-input]]
           h/html
           str))
  [:div])