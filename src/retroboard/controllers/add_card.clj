(ns retroboard.controllers.add-card
  (:require [hiccup2.core :as h]
            [retroboard.ui.components.testcols :as tc]
            [retroboard.wsapi.core :refer [pub]]))


(defn add-card-ctl [params]
  (pub (-> (tc/row [:div
                    (tc/col [:div {:hx-swap-oob "beforeend:#rows"} (tc/card :text (:text-input params))])
                    [:div {:class "d-grid"} (tc/button (:add-card-input params))]]
                   :id (:input-id params))
           h/html
           str))
  [:div])

(comment
  (add-card-ctl {:input-id "row-4" :add-card-input "row-2" :text-input "sf123421rfdsafwqerqwer"})
  :rcf)