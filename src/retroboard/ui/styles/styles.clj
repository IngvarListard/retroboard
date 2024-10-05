(ns retroboard.ui.styles.styles
  (:require [garden.core :refer [css] :as c]))

(defn blur-card []
  [[:.card.blur
    {:filter "blur(5px)"
     :transition "filter 0.3s ease"
     :user-select "none"
     :-webkit-user-select "none"  ;; Для Safari
     :-moz-user-select "none"      ;; Для Firefox
     :-ms-user-select "none"}]])

(defn styles []
  (css (blur-card)))