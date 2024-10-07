(ns retroboard.wsapi.core
  (:require [aleph.http :as http]
            [manifold.bus :as bus]
            [manifold.deferred :as d]
            [manifold.stream :as s]))
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
      (println "echo-handler")

      (s/connect socket socket))
    non-websocket-request))

(def boards (bus/event-bus))

(defn pub [text] (bus/publish! boards "board1" text))

(defn add-card-handler [req]
  (d/let-flow [conn (d/catch
                     (http/websocket-connection req)
                     (fn [_] nil))]
              (if-not conn
                non-websocket-request
                (do
                  (s/connect
                   (bus/subscribe boards "board1")
                   conn)
                  (println "subscribed")
                  (s/consume
                   #(bus/publish! boards "board1" %)
                   (->> conn
                        (s/map #(str name ": " %))
                        (s/buffer 100)))))))

(defn ws-handler [request]
  (case (:uri request)
    "/ws/add-card" (add-card-handler request)
    "/ws/echo" (echo-handler request)
    non-websocket-request))

(defn start-server [options]
  (let [s (http/start-server ws-handler options)]
    (println "WebSocket server started on ws://localhost:" (:port options))
    s))

(comment
  (defn s [] (http/start-server ws-handler {:port 5000}))
  (s)
  (def conn1 @(http/websocket-client "ws://localhost:5000/ws/add-card"))
  (def conn2 @(http/websocket-client "ws://localhost:5000/ws/add-card"))

  ;; Here we create two clients, and have them speak to each other
  (let [conn1 @(http/websocket-client "ws://localhost:5000/add-card")
        conn2 @(http/websocket-client "ws://localhost:5000/add-card")]

    ;; sign our two users in
    (s/put-all! conn1 ["shoes and ships" "Alice"])
    (s/put-all! conn2 ["shoes and ships" "Bob"])

    (s/put! conn1 "hello")

    (println @(s/take! conn1))                                  ;=> "Alice: hello"
    (println @(s/take! conn2))                                  ;=> "Alice: hello"

    (s/put! conn2 "hi!")

    (println @(s/take! conn1))                                  ;=> "Bob: hi!"
    (println @(s/take! conn2)))                                 ;=> "Bob: hi!"

  :rcf)