(ns retroboard.ui.core
  (:require [retroboard.ui.styles.styles :refer [styles]]))


(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta
    {:name "viewport", :content "width=device-width, initial-scale=1"}]
   [:title "Bootstrap demo"]
   [:link
    {:href "/css/bootstrap.min.css",
     :rel "stylesheet",
     :integrity
     "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"}]
   [:link {:rel "stylesheet", :href "/css/bootstrap-icons.min.css"}]
  ;;  [:style (retroboard-blur-card)]
   [:style (styles)]
   [:script
    {:src "/js/bootstrap.bundle.min.js",
     :integrity
     "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"}]
   [:script
    {:src "/js/htmx.min.js",
     :integrity
     "sha384-Y7hw+L/jvKeWIRRkqWYfPcvVxHzVzn5REgzbawhxAuQGwX1XWe70vji+VSeHOThJ"}]
   [:script
    {:src "/js/ws.js"}]])


(defn body [content]
  [:body content])


(defn page [content]
  [:!DOCTYPE
   {:lang "ru" :html ""}
   (head)
   (body content)])
