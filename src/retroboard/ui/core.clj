(ns retroboard.ui.core)


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
   [:script
    {:src "/js/bootstrap.bundle.min.js",
     :integrity
     "sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"}]])


(defn body [content]
  [:body content])


(defn page [content]
  [:!DOCTYPE
   {:lang "ru" :html ""}
   (head)
   (body content)])