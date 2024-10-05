(ns graaltest.hello
  (:gen-class)
  (:require [common.compjs :refer [->js]])
  (:import (org.graalvm.polyglot Context)))


(defn run-js [js-code]
  (let [context (-> (Context/newBuilder (into-array String ["js"]))
                    (.allowAllAccess true)
                    (.build))]
    (.eval context "js" js-code)))

(defn -main [& args]
  (println 1)

  (run-js (->js '(let [e "adsf"]
            (console.log e))))
  )