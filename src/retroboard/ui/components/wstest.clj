(ns retroboard.ui.components.wstest)

(defn wsexample []
  [:div
   {:hx-ext "ws", :ws-connect "ws://localhost:5000/chatroom"}
   [:div {:id "notifications"}]
   [:div {:id "chat_room"} "..."]
   [:form {:id "form", :ws-send ""} [:input {:name "chat_message"}]]])

(defn wsexample2 []
  [:div
   (comment "include htmx")
   [:h1 "Hello world"]
   (comment "websocket connection")
   [:div
    {:hx-ws "connect:/ws"
     :ws-connect "ws://localhost:4000/chatroom"
     :hx-ext "ws"}
    (comment
      "input for new messages, send a message to the backend\n      via websocket")
    [:form {:hx-ws "send:submit"} [:input {:name "chat_message"}]]]
   (comment "location for new messages from the server")
   [:div {:id "content"}]])