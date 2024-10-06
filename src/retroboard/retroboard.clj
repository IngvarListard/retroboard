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
            [retroboard.wsapi.core :refer [ws-handler start-server]]
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
  ;; (c/ANY "/ws/test" request (ws-handler request))
  ;; (c/GET "/api/forms/create-event" [] (ajax-views/get-create-event-view))
  ;; (c/POST "/api/forms/create-event" [] (fn [{:keys [form-params] :as _request}]
  ;;                                        (create-event! form-params)))
  ;; (c/GET "/api/forms/create-event/object-form"
  ;;   {form-params :query-string}
  ;;   (fn [& _] (-> form-params
  ;;                 form-decode
  ;;                 keywordize-keys
  ;;                 :select-object
  ;;                 ajax-views/get-record-form-view)))
  ;; (c/GET "/calendar" [] (r/response (calendar)))
  (route/resources "/")
  (route/not-found "Not Found"))



#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defonce server
  (do
    ;; (init-ws-api)
    (run-jetty
     (-> #'app-routes
         wrap-reload
         wrap-json-response
         wrap-params)
     {:port 3000
      :async? true
      :join? false})))



(defn -main
  [& _]

  ;; (run-jetty
  ;;      (-> #'app-routes
  ;;          wrap-reload
  ;;          wrap-json-response
  ;;          wrap-params)
  ;;      {:port 3000
  ;;       :async? true
  ;;       :join? false})
  (println 1))


(comment
  :rcf)