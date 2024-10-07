(ns retroboard.utils.common
   (:import [java.util Random]))

(defn random-string
  ([length]
   (let [chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
         random (Random.)]
     (apply str (repeatedly length #(nth chars (.nextInt random (count chars)))))))
  ([]
   (random-string 10))) 

(comment
  (random-string 30)
  (random-string)
  (random-string 10)
  
  :rcf)