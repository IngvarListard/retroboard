(ns retroboard.ui.components.testcols
  (:require [common.compjs :refer [->js]]))

(defn input []
  [:div
   {:class "mb-3" :id "input-form"}
   [:form
    [:input
     {:type "text",
      :class "form-control",
      :placeholder "Введите текст..."
      :name "text-input"}]
    [:div {:class "d-grid justify-content-end"}
     [:button {:class "btn btn-outline-secondary"
               :type "button"
               :hx-post "/api/board/add-card"
               :hx-params "text-input"
               :hx-target "#input-form"
               :hx-swap "outerHTML"}
      [:img {:class "icon" :src "/icons/check2.svg"}]]]]])

(defn row [content & {:keys [col-cnt] :or {col-cnt 1}}]
  [:div {:class (format "flex-fill row row-cols-%s" col-cnt)} content])

(defn col [content]
  [:div {:class "col my-1 "} content])

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

(defn card-row []
  (row [:div (col [:div (card)]) [:div {:class "d-grid"} (button)]]))

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
    (card-row)
    (card-row)
    (card-row)
    (card-row)]
   [:script {:type "text/javascript"} (->js '(console.log 123412341234))]])
