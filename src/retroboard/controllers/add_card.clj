(ns retroboard.controllers.add-card
  (:require [clojure.pprint :as pp]
            [retroboard.ui.components.testcols :as tc]))

(defn add-card-ctl [params]
  (println "add-card-ctl" params)
  (tc/row [:div (tc/col [:div (tc/card)]) [:div {:class "d-grid"} (tc/button)]]))