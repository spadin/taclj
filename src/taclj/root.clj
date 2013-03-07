(ns taclj.root
  (:use
    [compojure.core :only (defroutes GET)]
    [compojure.route :only (not-found)]
    [joodo.middleware.view-context :only (wrap-view-context)]
    [joodo.middleware.keyword-cookies :only (wrap-keyword-cookies)]
    [joodo.views :only (render-template render-html)]
    [joodo.controllers :only (controller-router)]))

(defroutes taclj-routes
  (GET "/" [] (render-template "index"))
  (controller-router 'taclj.controller)
  (not-found (render-template "not_found" :template-root "taclj/view" :ns `taclj.view.view-helpers)))

(def app-handler
  (->
    taclj-routes
    wrap-keyword-cookies
    (wrap-view-context :template-root "taclj/view" :ns `taclj.view.view-helpers)))

