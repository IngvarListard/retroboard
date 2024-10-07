(ns retroboard.controllers.add-card
  (:require [clojure.pprint :as pp]
            [hiccup2.core :as h]
            [retroboard.ui.components.testcols :as tc]
            [retroboard.wsapi.core :refer [pub]]))

(defn add-card-ctl [params]
  (println "add-card-ctl" (:input-id params))
  ;; (pub (-> [:div {:id "input-form-666"} "got ws div card"]
  ;;          h/html
  ;;          str))
  (pub (-> (tc/row [:div
                    (tc/col [:div (tc/card :text (:text-input params))])
                    [:div {:class "d-grid"} (tc/button)]]
                   :id (:input-id params))
           h/html
           str))
  ;; (tc/row [:div
  ;;          (tc/col [:div (tc/card :text (:text-input params))])
  ;;          [:div {:class "d-grid"} (tc/button)]])
  [:div])