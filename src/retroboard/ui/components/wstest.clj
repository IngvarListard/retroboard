(ns retroboard.ui.components.wstest)

(defn wsexample []
  [:div
   {:hx-ext "ws", :ws-connect "ws://localhost:5000/ws/add-card"}
   [:div {:id "cards"}]
   [:div {:id "chat_room"} "..."]
   [:form {:id "form", :ws-send ""} [:input {:name "chat_message"}]]])
