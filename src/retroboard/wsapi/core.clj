(ns retroboard.wsapi.core
  (:require [aleph.http :as http]
            [manifold.stream :as s]
            [clojure.pprint :refer [pprint]]))

(defn ws-handler [request]
  (let [ws @(http/websocket-connection request)]
    (s/consume
     (fn [msg]
       (println "Received message:" msg)
       (s/put! ws (str "Echo: "))
       (println "message sent")
       true) ; Эхо-сообщение
     ws)))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})

(defn echo-handler
  [req]
  (if-let [socket (try
                    @(http/websocket-connection req)
                    (catch Exception e
                      (fn [] 
                        (println "Websocket connection error: " e))))]
    (do
      (pprint req)
      (s/connect socket socket))
    non-websocket-request))

(defn start-server []
  (let [s (http/start-server echo-handler {:port 5000})]
    (println "WebSocket server started on ws://localhost:5000")
    s))

