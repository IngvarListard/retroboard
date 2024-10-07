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
            [retroboard.ui.components.testcols :refer [manycols input]]
            [retroboard.wsapi.core :as ws]
            [retroboard.ui.components.wstest :refer [wsexample]]
            [clojure.walk :refer [keywordize-keys]]
            [retroboard.controllers.add-card :refer [add-card-ctl]])
  (:gen-class))


(c/defroutes app-routes
  (c/GET "/" [] (r/response (str (h/html (page (home-page))))))
  (c/GET "/cols" [] (r/response (str (h/html (page (manycols))))))
  (c/GET "/wsexample" [] (r/response (str (h/html (page (wsexample))))))
  (c/GET "/api/board/add-card" [] (r/response (str (h/html (input)))))
  (c/POST "/api/board/add-card" request (-> request
                                            :params
                                            keywordize-keys
                                            add-card-ctl
                                            h/html
                                            str
                                            r/response))
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