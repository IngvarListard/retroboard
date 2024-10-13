(ns common.compjs
  (:require [squint.compiler :as squint]
            [hiccup2.core :as h]))

(def state (atom nil))

(defn ->js [form]
  (let [res (squint.compiler/compile-string* (str form))]
    (reset! state res)
    (:body res)))

(defn with-context-bindings
  [form bindings]
  (reduce concat '(let) [[bindings] [form]]))

(defn raw-hiccup
  "Convert hiccup to raw string"
  [el & raw-elems]
  (let [do-format-raw-string #(apply format % raw-elems)]
    (-> (h/html el)
        (str)
        (do-format-raw-string)
        (h/raw))))

(comment
  (str (raw-hiccup [:div {:a "%s"}] "{'a': 'b'}"))
  :rcf)