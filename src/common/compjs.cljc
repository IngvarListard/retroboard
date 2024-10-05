(ns common.compjs
  (:require [squint.compiler :as squint]))

(def state (atom nil))

(defn ->js [form]
  (let [res (squint.compiler/compile-string* (str form))]
    (reset! state res)
    (:body res)))

(defn with-context-bindings
  [form bindings]
  (reduce concat '(let) [[bindings] [form]]))
