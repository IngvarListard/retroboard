(ns graaltest.hello
  (:gen-class)
  (:import (org.graalvm.polyglot Context)))


(defn run-js-2 [js-code]
  (let [context (-> (Context/newBuilder (into-array String ["js"]))
                    (.allowAllAccess true)
                    (.build))]
    (.eval context "js" js-code)))

(defn -main [& args]
  (println 1)
  (run-js-2 "console.log('Hello from JavaScript running on GraalVM!');"))