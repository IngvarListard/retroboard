(ns retroboard.retroboard
  (:require [compojure.core :as c]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :as r]
            [hiccup2.core :as h]
            [retroboard.ui.components.home :refer [home-page]]
            [retroboard.ui.core :refer [page]]
            [retroboard.ui.components.testcols :refer [manycols]]
            [retroboard.wsapi.core :as ws]
            [retroboard.ui.components.wstest :refer [wsexample]]
            [clojure.walk :refer [keywordize-keys]]
            [retroboard.views.append-test :refer [some-list append]]
            [retroboard.ui.components.board :refer [bboard add-card-input do-add-card]]
            [retroboard.storage.boards :refer [init-board-storage board-storage-with-defaults]])
  (:gen-class))

(defonce b-stoarge (init-board-storage))

(comment
  (defn new-board
    [name theme]
    {name
     {:board-theme theme
      :cols
      []}})

  (def bbb (board-storage-with-defaults))
  (def sstorage (init-board-storage))
  (get-in @sstorage [:board-1 :board-theme])
  (swap! sstorage assoc-in [:board-3 :board-theme] "dark")
  :rcf)

(defn do-atom []
  (pr-str @b-stoarge))
(c/defroutes app-routes
  (c/GET "/" [] (r/response (str (h/html (page (home-page))))))
  (c/GET "/cols" [] (r/response (str (h/html (page (manycols))))))
  (c/GET "/wsexample" [] (r/response (str (h/html (page (wsexample))))))


  ;; TODO: malli схемы на входные параметры для связи компонентов
  (c/GET "/board" [] (r/response (str (h/html (page (bboard))))))
  (c/GET "/api/board/add-card-input" request (r/response (str (h/html (add-card-input request)))))
  (c/POST "/api/board/add-card-input" request (-> request
                                                  :params
                                                  keywordize-keys
                                                  do-add-card
                                                  h/html
                                                  str
                                                  r/response))


  (c/POST "/api/board/append" request (r/response (str (h/html (append request)))))
  (c/GET "/append-test" request (r/response (str (h/html (page (some-list))))))
  (c/POST "/api/doatom" _ (r/response (str (h/html (do-atom)))))
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