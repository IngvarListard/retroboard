(ns retroboard.views.append-test)

(defn some-list
  []
  [:div {:id "list"
         :class "list-class"
         :data-column-number "1"}
   [:input {:type "hidden" :name "top-level" :value "top-level-input" :id "top-level-id"}]
   [:div
    [:div
     [:div {:class "item"} "inner list"]
     [:div {:class "item"} "inner list"]
     [:div {:class "item"} "inner list"]
     [:div {:class "item"} "inner list"]
     [:div {:id "to-swap"} "inner list"]]
    [:input {:type "hidden" :name "test" :value "wtf-card-input" :id "test-id" :data-column-number "2"}]
    [:input {:type "hidden" :name "jest" :value "jest-val" :id "jest-id"}]
    [:input {:type "hidden" :name "west" :value "west-val" :id "west-id"}]
    [:input {:type "hidden" :name "jsvals" :hx-vals {"'a'" "'b'"} :value "west-val" :id "west-id"}]
    [:button
     {;;  :type "button" 
      :class "btn btn-outline-secondary"
      :hx-include "closest #list"
      :hx-parmas "data"
      :hx-post "/api/board/append"}
     [:img {:src "/icons/plus-lg.svg" :class "icon"}]]]])


(defn append
  [request]
  (println (:params request))
  [:div
   {:hx-swap-oob "beforeend:#list"}
   [:div
    {:class "item"}
    "appended"]])

(defn append-2 []
  [:div
   {:id "to-swap" :hx-swap-oob true}
   [:div
    {:class "item"}
    [:br]
    "swapped"
    [:br]]])