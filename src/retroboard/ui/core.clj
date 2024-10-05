(ns retroboard.ui.core
  (:require [retroboard.ui.styles.styles :refer [styles]]
            [cheshire.core :as json]
            [hiccup2.core :as h]
            [common.compjs :refer [->js with-context-bindings]]))


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
    {:src "/js/ws.js"}]
  ;;  [:script
  ;;   {:src "/js/squint.js"}]
   [:script {:type "importmap"}
    (h/raw (json/generate-string
            {:imports
             {"squint-cljs/src/squint/core.js" "/js/squint.js"}}))]
   [:script {:type "module" :async "true"}
    (h/raw "globalThis.squint_core = await import('squint-cljs/src/squint/core.js');")]])


(defn body [content]
  [:body content])


(defn page [content]
  [:!DOCTYPE
   {:lang "ru" :html ""}
   (head)
   (body content)])

(comment

  (println
   (->js '(let [elt (js/document.getElementById "counter")
                val (-> (.-innerText elt) parse-long)]
            (set! elt -innerText (inc val)))))


  (println
   (->js
    (pr-str (with-context-bindings
              '(do
                 (def DAYS ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"])
                 (defn app []
                   {:init (fn []
                            (this.initDate)
                            (this.getNoOfDays))
                    :month ""
                    :year ""
                    :no_of_days []
                    :blankdays []
                    :openEventModal false
                    :isToday (fn [date]
                               (this-as
                                $
                                (let [today (new Date)
                                      d (new Date $.year $.month date)]
                                  (identical? (-> today .toDateString) (-> d .toDateString)))))}))
              ['a 1 'b 2 'c 3]))))

  (->js
   '(do
      (def DAYS ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"])
      (defn app []
        {:init (fn []
                 (this.initDate)
                 (this.getNoOfDays))
         :month ""
         :year ""
         :no_of_days []
         :blankdays []
         :openEventModal false
         :isToday (fn [date]
                    (this-as
                     $
                     (let [today (new Date)
                           d (new Date $.year $.month date)]
                       (identical? (-> today .toDateString) (-> d .toDateString)))))})))

  :rcf)
