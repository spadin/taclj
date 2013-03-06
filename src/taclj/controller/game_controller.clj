(ns taclj.controller.game-controller
  (:use [ticlj.game.protocol :only [empty-board-state
                                    set-mark-at-index
                                    next-possible-mark]])
  (:require [compojure.core :refer (GET defroutes)]
            [joodo.views :refer (render-template)]
            [joodo.middleware.request :refer (*request*)]
            [ring.util.response :refer [redirect]]
            [ticlj.game] [ticlj.player]))

(defn- render-not-found [error-msg]
  {:status 404
   :headers {}
   :body (render-template "not_found" :error error-msg)})

(defn- has-game-setup-params? []
  (and
    (not (empty? (:game-type (:params *request*))))
    (not (empty? (:x-player (:params *request*))))
    (not (empty? (:o-player (:params *request*))))))

(defn- find-first-that-contains [s coll]
  (first (filter #(.contains (:value %) s) coll)))


(defn get-game-of-uri-value [s]
  (let [game-str (:value (find-first-that-contains s ticlj.game/available-game-types))]
    (ticlj.game/get-game-of-type game-str)))

(defn get-player-of-uri-value [s]
  (let [player-str (:value (find-first-that-contains s ticlj.player/available-player-types))]
    (ticlj.player/get-player-of-type player-str)))

(defn convert-string-to-board-state [s]
  (reduce (fn [memo val] (conj memo (keyword (if (= "-" (str val)) "#" (str val))))) [] s))

(defn convert-board-state-to-string [board-state]
  (reduce #(str %1 (clojure.string/replace (name %2) #"#" "-")) "" board-state))

(defn get-board-str [game-type-str]
  (let [param-board (:board (:params *request*))]
    (if (nil? param-board)
      (let [game (get-game-of-uri-value game-type-str)
            board-state (empty-board-state game)]
        (convert-board-state-to-string board-state))
      param-board)))

(defn- game-route [game-type x-player o-player board-str]
  (str "/game/" game-type
       "/" x-player
       "/" o-player
       "/show?board-str=" board-str))

(defn- game-route-from-params []
  (game-route (:game-type (:params *request*))
              (:x-player (:params *request*))
              (:o-player (:params *request*))
              (get-board-str (:game-type (:params *request*)))))

(defroutes game-controller
  (GET "/game" []
    (if (has-game-setup-params?)
      (redirect (game-route-from-params))
      (redirect "/")))

  (GET "/game/:game-type/:x-player/:o-player/show" [game-type x-player o-player]
    (render-template "game/index" {:game-type game-type
                                   :x-player x-player
                                   :o-player o-player
                                   :board-str (:board-str (:params *request*))}))

  (GET "/game/:game-type/:x-player/:o-player/move" [game-type x-player o-player]
    (let [choice (Integer/parseInt (:choice (:params *request*)))
          board-state (convert-string-to-board-state (:board-str (:params *request*)))
          game (get-game-of-uri-value game-type)
          player-str-coll {:X x-player :O o-player}
          player-str (get player-str-coll (next-possible-mark game board-state))
          player (get-player-of-uri-value player-str)
          new-board-state (set-mark-at-index game board-state choice)
          new-board-str (convert-board-state-to-string new-board-state)]
    (redirect (game-route game-type x-player o-player new-board-str)))))
