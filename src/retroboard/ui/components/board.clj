(ns retroboard.ui.components.board
  (:require [hiccup2.core :as h]
            [retroboard.storage.add :as a]
            [retroboard.storage.boards :refer [new-card new-col]]
            [retroboard.utils.common :as u]
            [common.compjs :refer [raw-hiccup hx-vals]]
            [retroboard.wsapi.board-topic :as bt]))

(def n
  "Сквозные имена компонентов"
  {::col-number "col-number"
   ::board "board"})

(def api
  {::get-add-card-input "/api/board/add-card-input"
   ::add-card "/api/board/add-card"
   ::delete-card "/api/board/delete-card"
   ::add-col "/api/board/add-col"})

(def icons
  {::check "/icons/check2.svg"
   ::plus "/icons/plus-lg.svg"
   ::x "/icons/x.svg"})

(def default-board-key :board-1)

(defn add-card-input
  "Поле ввода для добавления новой карты в колонку"
  [{:keys [col-number]}]
  (let [input-id (str "add-card-input-" col-number)
        placeholder "Введите текст..."
        input-name "text-input"]
    [:div {:id input-id}
     [:div {:class "mb-3"}
      [:div
       [:input
        {:type "text",
         :class "form-control",
         :placeholder placeholder
         :name input-name}]
       (let [swap-target (str "#" input-id)
             include-vals (hx-vals {:col-number col-number})
             hx-include (format "[name='%s']" input-name)]
         [:div {:class "d-grid justify-content-end"}
        ;; raw для hx-include, hx-vals
          (raw-hiccup
           [:button
            {:class "btn btn-outline-secondary"
             :type "button"
             :hx-include hx-include
             :hx-target swap-target
             :hx-vals include-vals
             :hx-post (-> api ::add-card)}
            [:img {:class "icon" :src (-> icons ::check)}]])])]]]))

(comment
  (def selector-example
    "Пример поиска значений по подстроке. Ищет ближайший элемент
     класса row. Из детей выбирает input'ы с id=подстрока col-*"
    {:hx-include "closest .row, [id^='col-']"})
  :rcf)

(defn get-add-card-input-button
  "Кнопка для получения формы ввода для добавления новой карты в колонку"
  [col-number]
  (let [include-vals (hx-vals {:col-number col-number})]
    (raw-hiccup
     [:div
      [:button
       {:class "btn btn-outline-secondary"
        :id "add-card-button"
        :hx-vals include-vals
        :hx-get (-> api ::get-add-card-input)
        :hx-swap "outerHTML"}
       [:img {:class "icon" :src (-> icons ::plus)}]]])))

(defn board-card
  [idx {:keys [id text]} col-number]
  [:div
   {:id id
    :hx-swap-oob "true"
    :class "card p-2 m-2 position-relative"}
   (let [include-vals (hx-vals {:card-number idx
                                :col-number col-number
                                :card-id id})]
     (raw-hiccup
      [:button
       {:type "button"
        :aria-label "Close"
        :hx-vals include-vals
        :hx-post (-> api ::delete-card)
        ;; :hx-target "closest .card"
        :hx-swap "none"
        :class "btn position-absolute top-0 end-0 m-0 align-center btn-sm"}
       [:img {:class "icon" :src (-> icons ::x)}]]))
   [:div text]]) ; Центрирование текста внутри карточки

(defn board-column
  [idx {:keys [name cards]}]
  [:div {:id idx :class "flex-fill row"}
   [:div {:id (str "col-" idx)}
    [:input {:type "hidden" :name (::col-number n) :value idx}]
    [:p name]
    (into
     [:div]
     (vec (map-indexed #(board-card %1 %2 idx) cards)))]
   (get-add-card-input-button idx)])

(defn retroboard
  "Основная доска"
  [{:keys [name theme cols]}]

  [:div {:id "retroboard" :class "container-fluid"}
   [:input {:type "hidden" :name (::board n) :value name}]

   [:div {:id "board-theme-text" :class "card-body"}
    [:h5 {:id "board-theme-title" :class "card-title"}
     name]
    [:p {:id "board-theme-desc" :class "card-text"}
     theme]]
   [:div {:class "top-0 end-0 m-0 align-center "}
    [:button {:class "btn btn-primary btn-sm"
              :hx-post (-> api ::add-col)}
     "Add column"]]

   (into
    [:div {:id "retroboard-cards"
           :class "row row-cols-4"
           :hx-ext "ws"
          ;; TODO: ws-connect порт должен быть такой же как у основного приложения
           :ws-connect "ws://localhost:5000/ws/card-operations"}]

    (vec (map-indexed board-column cols)))])


;; TODO: перенести функцию в board/views.clj
(defn add-card!
  "Отправить карту через websocket"
  [{:keys [col-number text-input board-key]} board & {:keys [bkey]}]

  (let [col-number (-> col-number u/parse-int)
        card (new-card :text text-input)
        board-key (or board-key bkey default-board-key)
        new-card-idx (count (get-in @board [board-key :cols col-number :cards]))
        new-board-card (board-card
                        new-card-idx
                        {:id (u/random-string) :text text-input} col-number)]

    (swap! board #(a/add-in-place % [board-key :cols col-number :cards] card))
    (bt/pub! (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
                  new-board-card]
                 h/html
                 str))

    (get-add-card-input-button col-number)))

(defn add-col!
  [{:keys [board-key]} board]
  (let [col (new-col)
        new-col (board-column (:id col) col)]

    (swap! board #(a/add-in-place % [board-key :cols] col))
    (bt/pub! (-> [:div {:hx-swap-oob (str "beforeend:#retroboard-cards")}
                  new-col]
                 h/html
                 str))
    "OK"))

(comment
  :rcf)