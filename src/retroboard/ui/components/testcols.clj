(ns retroboard.ui.components.testcols
  (:require [common.compjs :refer [->js]]
            [retroboard.utils.common :refer [random-string]]
            [hiccup2.core :as h]))

;; параметры для отрисовки инпута
(defn input []
  (let [rand-str (random-string)
        id (str "form-id-" rand-str)
        room-id (str "room-id-" rand-str)
        operation-id (str "operation-id-" rand-str)
        input-id (str "input-id-" rand-str)
        text-input-id (str "text-input-id-" rand-str)]
    [:div
     [:div
   ;; TODO: динамический id формы
      {:class "mb-3" :id id}
      [:div
       [:input {:type "hidden" :name "room_id" :value "retroboard" :id room-id}]
       [:input {:type "hidden" :name "operation" :value "add-card" :id operation-id}]
       [:input {:type "hidden" :name "input-id" :value id :id input-id}]
       [:input
        {:type "text",
         :class "form-control",
         :placeholder "Введите текст..."
         :name "text-input"
         :id text-input-id}]
       [:div {:class "d-grid justify-content-end"}
        [:button {:class "btn btn-outline-secondary"
                  :type "button"
                  :hx-post "/api/board/add-card"
                  :hx-include (format "#%s, #%s, #%s, #%s" room-id operation-id input-id text-input-id)
                  ;; :hx-params "*"
              ;;  :hx-target "#input-form"
              ;;  :hx-swap "outerHTML"
                  }
         [:img {:class "icon" :src "/icons/check2.svg"}]]]]]
     [:script {:type "text/javascript"} (h/raw "function aaa () {
        window.onload = function() {
            const inputField = document.getElementById(\"myInput\");
            inputField.addEventListener(\"keydown\", function(event) {
                if (event.key === \"Enter\") {
                    event.preventDefault(); // Отключаем стандартное поведение
                    console.log(\"Enter нажата, но форма не отправлена.\");
                }
            });
        };
                                              };
          aaa()")]]))

(defn row [content & {:keys [col-cnt id] :or {col-cnt 1 id "999"}}]
  [:div {:class (format "flex-fill row row-cols-%s" col-cnt) :id id} content])

(defn col [content]
  [:div {:class "col my-1 "} content])

(def default-text "Some quick example text to build on the card title and make up the bulk of the card content.")

(defn card [& {:keys [text] :or {text nil}}]
  (let [text (or text default-text)]
    [:div
     {:class "card text-black"}
     [:div
      {:class "m-1"}
      [:p
       {:class "card-text", :style "font-size:14px;"}
       text]]]))

(defn button []
  [:button
   {:class "btn btn-outline-secondary"
    :type "button"
    :hx-get "/api/board/add-card"
    :hx-swap "outerHTML"}
   [:img {:class "icon" :src "/icons/plus-lg.svg"}]])

(defn card-row [& {row-id :row-id}]
  (row (if row-id
         [:div {:id row-id :hx-swap-oob "morphdom"}
          (col [:div (card)])
          [:div {:class "d-grid"}
           (button)]]
         (button))))

(defn manycols []
  [:div
   {:class "container-fluid"}
   [:div
    {:class "card-body"}
    [:h5 {:class "card-title"} "Theme title"]
    [:p
     {:class "card-text"}
     "Theme"]]
   [:div {:class "row row-cols-4" :hx-ext "ws", :ws-connect "ws://localhost:5000/ws/add-card"}
    (card-row)
    (card-row)
    (card-row)
    (card-row)]
  ;;  [:script {:type "text/javascript"} (h/raw "function aaa () {
  ;;       window.onload = function() {
  ;;           const inputField = document.getElementById(\"myInput\");
  ;;           inputField.addEventListener(\"keydown\", function(event) {
  ;;               if (event.key === \"Enter\") {
  ;;                   event.preventDefault(); // Отключаем стандартное поведение
  ;;                   console.log(\"Enter нажата, но форма не отправлена.\");
  ;;               }
  ;;           });
  ;;       };
  ;;                                             };
  ;;         aaa()")]
   ])


