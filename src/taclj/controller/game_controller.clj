(ns taclj.controller.game-controller
  (:require [compojure.core :refer (GET defroutes)]
            [joodo.views :refer (render-template)]
            [joodo.middleware.request :refer (*request*)]
            [ring.util.response :refer [redirect]]))

(defn- render-not-found [error-msg]
  {:status 404
   :headers {}
   :body (render-template "not_found" :error error-msg)})

(defn has-game-setup-params? []
  (and
    (not (empty? (:game-type (:params *request*))))
    (not (empty? (:x-player (:params *request*))))
    (not (empty? (:o-player (:params *request*))))))

(defn game-route []
  (str "/game/"
       (:game-type (:params *request*))
       "/"
       (:x-player (:params *request*))
       "/"
       (:o-player (:params *request*))
       "/play"))

(defroutes game-controller
  (GET "/game" []
    (if (has-game-setup-params?)
      (redirect (game-route))
      (redirect "/")))

  (GET "/game/:game-type/:x-player/:o-player/play" [game-type x-player o-player]
    (render-template "game/index" {:game-type game-type
                                   :x-player x-player
                                   :o-player o-player})))
