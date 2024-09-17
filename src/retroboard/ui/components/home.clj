(ns retroboard.ui.components.home)



(defn add-card-button []
  [:button
   {:type "button" :class "btn btn-outline-secondary"}
   [:img {:src "/icons/plus-lg.svg" :class "icon"}]])

(defn table-col []
  [:td
   [:div
    {:class "card text-black"}
    [:div
     {:class "m-1"}
     [:p
      {:class "card-text" :style {:font-size "14px"}}
      "Some quick example text to build on the card title and make up the bulk of the card content."]]]])

(defn add-button-col []
  [:td 
   (add-card-button)])

(defn home-page []
  [:div
   [:div
    {:class "container"}
    [:table
     {:class "table table-bordered table-striped"}
     [:thead
      [:tr
       [:th {:scope "col"}]
       [:th {:scope "col"}]
       [:th {:scope "col"}]]]
     [:tbody
      [:tr
       [:td
        {:colspan "3"}
        [:div
         {:class "card bg-primary text-white mb-3"}
         (add-card-button)
        ;;  [:img
        ;;   {:src "your_image_url",
        ;;    :class "card-img-top",
        ;;    :alt "Card image cap"}
        ;;   ]
         [:div
          {:class "card-body"}
          [:h5 {:class "card-title"} "Primary Card Title"]
          [:p
           {:class "card-text"}
           "Some quick example text to build on the card title and make up the bulk of the card content."]]]]]
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (table-col)))
      (into [:tr] (for [_ (range 3)] (add-button-col)))]]]])
