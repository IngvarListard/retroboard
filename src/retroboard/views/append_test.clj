(ns retroboard.views.append-test)


(defn some-list
  []
  [:div
   [:div
    {:id "list"}
    [:div {:class "item"} "inner list"]
    [:div {:class "item"} "inner list"]
    [:div {:class "item"} "inner list"]
    [:div {:class "item"} "inner list"]
    [:div {:id "to-swap"} "inner list"]]
   [:button
    {:type "button" :class "btn btn-outline-secondary"
     :hx-post "/api/board/append"}
    [:img {:src "/icons/plus-lg.svg" :class "icon"}]]])

(defn append []
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