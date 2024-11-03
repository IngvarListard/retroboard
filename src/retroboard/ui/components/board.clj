(ns retroboard.ui.components.board
  (:require [hiccup2.core :as h]
            [retroboard.wsapi.core :refer [boards]]
            [manifold.bus :as bus]
            [retroboard.storage.add :as a]
            [retroboard.storage.boards :refer [new-card]]
            [retroboard.utils.common :as u]
            [common.compjs :refer [raw-hiccup hx-vals]]))

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

;; board schema
{:board-1
 {:name "Board 1"
  :theme "theme"
  :id 1
  :cols
  [{:id 1
    :name "Column theme"
    :cards [(new-card :text "card col 1")]}
   {:id 2
    :name "Column theme 2"
    :cards [(new-card :text "card col 2")]}
   {:id 3
    :name "Column theme 3"
    :cards [(new-card :text "card col 3")]}]}}


(defn add-card-input
  "Поле ввода для добавления новой карты в колонку"
  [{:keys [col-number]}]
  (let [col-id (format "add-card-input-%s" col-number)]
    [:div
     {:id col-id}
     [:div
      {:class "mb-3"}
      [:div
       [:input
        {:type "text",
         :class "form-control",
         :placeholder "Введите текст..."
         :name "text-input"
         :id (:text-input i)}]
       [:div {:class "d-grid justify-content-end" :id "swap-input"}
        (raw-hiccup
         [:button
          {:class "btn btn-outline-secondary"
           :type "button"
           :hx-include "closest .row, [id^='col-']"
           :hx-swap "outerHTML"
           :hx-target (str "#" col-id)
           :hx-vals (hx-vals {:testva "testval2"})
           :hx-post "/api/board/add-card-input"}

          [:img {:class "icon" :src "/icons/check2.svg"}]])]]]]))

(defn get-add-card-input-button
  "Кнопка для получения формы ввода для добавления новой карты в колонку"
  []
  (raw-hiccup
   [:div {}
    [:button
     {:class "btn btn-outline-secondary"
      :id "add-card-button"
    ;;  :hx-include "closest .row"
      ;; :hx-include (format "[name='test-name'], [name=%s]" (::col-number n))

      :hx-include "closest .row, [id^='col-']"
      :hx-get "/api/board/add-card-input"
      :hx-swap "outerHTML"}
     [:img {:class "icon" :src "/icons/plus-lg.svg"}]]]))

(def default-board "board2")

(defn board-card
  [{:keys [id text]}]
  [:div
   {:id id :class "card"}
   [:div text]])

(defn board-column
  [idx {:keys [name cards]}]
  [:div {:id idx :class "flex-fill row"}
   [:div {:id (str "col-" idx)}
    [:input {:type "hidden" :name (::col-number n) :value idx}]
    ;; [:input {:type "hidden" :name "current-col-number" :value idx}]
    [:p name]
    ;; Элемент: карты в колонке
    (into
     [:div]
     (vec (map board-card cards)))]
   (get-add-card-input-button)])

(defn bboard
  "Основная доска"
  [{:keys [name theme id cols]}]

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
           :ws-connect "ws://localhost:5000/ws/card-operations"}
     #_[:input {:type "hidden" :name "test-name" :value id}]]

    (vec (map-indexed board-column cols)))])

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
  (println "do-add-card params" params)
  (let [col-number (-> col-number u/parse-int)]
    (swap! board #(a/add-in-place % [:board-1 :cols col-number :cards] (new-card :text text-input)))

    (pub! (-> [:div {:hx-swap-oob (str "beforeend:#col-" col-number)}
               [:div text-input]]
              h/html
              str))
    (get-add-card-input-button)))