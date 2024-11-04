(ns retroboard.ui.components.board
  (:require [hiccup2.core :as h]
            [retroboard.wsapi.core :refer [board-transport pub!]]
            [retroboard.storage.add :as a]
            [retroboard.storage.boards :refer [new-card]]
            [retroboard.utils.common :as u]
            [common.compjs :refer [raw-hiccup hx-vals]]))

(def n
  "Сквозные имена компонентов"
  {::col-number "col-number"
   ::board "board"})

(def api
  {::add-card-input "/api/board/add-card-input"})

(def icons
  {::check "/icons/check2.svg"
   ::plus "/icons/plus-lg.svg"
   ::x "/icons/x.svg"})

(def retroboard-topic "board2")
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
             :hx-post (-> api ::add-card-input)}
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
        :hx-get (-> api ::add-card-input)
        :hx-swap "outerHTML"}
       [:img {:class "icon" :src (-> icons ::plus)}]]])))

(defn board-card
  [idx {:keys [id text]}]
  [:div
   {:id id
    :class "card p-2 m-2 position-relative"}
   [:button
    {:type "button"
     :aria-label "Close"
     :class "btn position-absolute top-0 end-0 m-0 align-center btn-sm"}
    [:img {:class "icon" :src (-> icons ::x)}]]
   [:div text]]) ; Центрирование текста внутри карточки

(defn board-column
  [idx {:keys [name cards]}]
  [:div {:id idx :class "flex-fill row"}
   [:div {:id (str "col-" idx)}
    [:input {:type "hidden" :name (::col-number n) :value idx}]
    [:p name]
    (into
     [:div]
     (vec (map-indexed board-card cards)))]
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
     ;; TODO: row-cols-#
   (into
    [:div {:id "retroboard-cards"
           :class "row row-cols-4"
           :hx-ext "ws"
          ;; TODO: ws-connect порт должен быть такой же как у основного приложения
           :ws-connect "ws://localhost:5000/ws/card-operations"}]

    (vec (map-indexed board-column cols)))])

(comment
  (def asdf {:id 1, :name "Column theme", :cards [{:id "e4e84d21-be16-4f0a-87d7-57130fda13c2", :text "card col 1"}]})
  (into
   [:div {:id "row" :class "flex-fill row"}]
   (vec (map-indexed board-column [asdf])))
  :rcf)
;; TODO: принимать название канала в boards


(defn add-card!
  "Отправить карту через websocket"
  [{:keys [col-number text-input board-key]} board & {:keys [bkey]}]

  (let [col-number (-> col-number u/parse-int)
        card (new-card :text text-input)
        board-key (or board-key bkey default-board-key)
        new-card-idx (count (get-in @board [board-key :cols col-number :cards]))]

    (swap! board #(a/add-in-place % [board-key :cols col-number :cards] card))
    (pub! retroboard-topic
          (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
               (board-card new-card-idx {:text text-input})]
              h/html
              str))

    (get-add-card-input-button col-number)))