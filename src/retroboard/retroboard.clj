(ns retroboard.retroboard
  (:require [compojure.core :as c]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [retroboard.views.board :as bv]
            [retroboard.wsapi.core :as ws]
            [retroboard.storage.boards :refer [add-board new-card]])

  (:gen-class))

(defonce board-storage (add-board))

(comment
  ;; добавление карточек в колонки. Проверка работы после перезагрузки страницы
  ;; такое поведение нужно добавить в добавление карточки
  (require '[retroboard.storage.add :as a])
  (swap! board-storage #(a/add-in-place % [:board-1 :cols 2 :cards] (new-card :text "qwerdsafqwer") :idx 0))
  :rcf)

(def default-board :board-1)

(c/defroutes app-routes
  ;; TODO: malli схемы на входные параметры для связи компонентов
  ;; TODO: board name as storage key
  (c/GET "/board" request (bv/get-board request board-storage :board-key default-board))
  (c/GET "/api/board/add-card-input" request (bv/get-add-card-input-button request))
  (c/POST "/api/board/add-card-input" request (bv/do-add-card! request board-storage :board-key default-board))


  ;; (c/POST "/api/board/delete-card" request (-> request :params keywordize-keys delete-card h/html str r/response))
  (route/resources "/")
  (route/not-found "Not Found"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defonce server
  (run-jetty
   (-> #'app-routes
       wrap-reload
       wrap-json-response
       wrap-params)
   {:port 3000
    :async? true
    :join? false}))

(defonce ws-server
  (ws/start-server {:port 5000}))

(defn -main
  [& _])

(comment
  :rcf)