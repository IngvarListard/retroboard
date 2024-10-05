(ns retroboard.ui.components.testcols
  (:require [common.compjs :refer [->js]]))

(defn input []
  [:div
   {:class "mb-3"}
   [:label
    {:for "exampleFormControlInput1", :class "form-label"}
    "Введите текст"]
   [:input
    {:type "text",
     :class "form-control",
     :id "exampleFormControlInput1",
     :placeholder "Введите текст..."}]])

(defn row [content & {:keys [col-cnt] :or {col-cnt 1}}]
  [:div {:class (format "flex-fill row row-cols-%s" col-cnt)} content])

(defn col [content]
  (println content)
  [:div {:class "col"} content])

(defn card []
  [:div
   {:class "card text-black"}
   [:div
    {:class "m-1"}
    [:p
     {:class "card-text", :style "font-size:14px;"}
     "Some quick example text to build on the card title and make up the bulk of the card content."]]])

(defn button []
  [:button
   {:class "btn btn-outline-secondary" :type "button" :hx-get "/api/board/add-card" :hx-swap "outerHTML"}
   [:img {:class "icon" :src "/icons/plus-lg.svg"}]])

(defn manycols []
  [:div
  {:class "container-fluid"}
   [:div
    {:class "card-body"}
    [:h5 {:class "card-title"} "Theme title"]
    [:p
     {:class "card-text"}
     "Theme"]]
   [:div {:class "row row-cols-4"}
    (row [:div (col [:div (card)]) [:div {:class "d-grid"} (button)]])
    (row [:div (col [:div (card)]) [:div {:class "d-grid"} (button)]])
    (row [:div (col [:div (card)]) [:div {:class "d-grid"} (button)]])
    (row [:div (col [:div (card)]) [:div {:class "d-grid"} (button)]])
    ]
   [:script {:type "text/javascript"} (->js '(console.log 123412341234))]])
