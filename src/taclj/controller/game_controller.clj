(ns taclj.controller.game-controller
  (:use [ticlj.game.protocol :only [empty-board-state
                                    set-mark-at-index
                                    next-possible-mark
                                    gameover? winner]]
        [ticlj.player.protocol :only [move]])
  (:require [compojure.core :refer (GET POST defroutes)]
            [joodo.views :refer (render-template)]
            [joodo.middleware.request :refer (*request*)]
            [clojure.data.json :as json]
            [ring.util.response :refer [redirect]]
            [ticlj.game] [ticlj.player]))

;; Converts map and vector keys to keywords
(defn keyify [target]
  (if (map? target)
    (into {}
      (for [[k v] target]
        [(keyword k) (keyify v)]))
    (if (vector? target)
      (vec (map keyify target))
      target)))

(defn- render-not-found [error-msg]
  {:status 404
   :headers {}
   :body (render-template "not_found" :error error-msg)})

(defn- has-game-setup-params? []
  (and
    (not (empty? (:game-type (:params *request*))))
    (not (empty? (:x-player  (:params *request*))))
    (not (empty? (:o-player  (:params *request*))))))

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

(defn empty-board-str [game-type-str]
  (let [game (get-game-of-uri-value game-type-str)
        board-state (empty-board-state game)]
    (convert-board-state-to-string board-state)))

(defn get-board-str [game-type-str]
  (let [param-board (:board (:params *request*))]
    (if (nil? param-board)
      (empty-board-str game-type-str)
      param-board)))

(defn- game-route [game-type x-player o-player board-str]
  (str "/game/" game-type
       "/" x-player
       "/" o-player
       "/show?board-str=" board-str))

(defn- game-route-from-params []
  (let [params (:params *request*)
        game-type (:game-type params)
        x-player (:x-player params)
        o-player (:o-player params)
        board-str (get-board-str game-type)]
    (game-route game-type x-player o-player board-str)))

(defn game-cookie-map [game-type x-player o-player board-str]
  {:game {:value
          (json/write-str {:game-type game-type
                           :x-player x-player
                           :o-player o-player
                           :board-str board-str})}})

(defn new-game-cookie []
  (let [params (:params *request*)
        game-type (:game-type params)
        x-player (:x-player params)
        o-player (:o-player params)
        board-str (empty-board-str game-type)]

    (game-cookie-map game-type x-player o-player board-str)))

(defn get-game-cookie [source]
  (let [game-cookie-str (if (and (not (nil? (:cookies source)))
                                 (not (nil? (:game (:cookies source))))
                                 (not (= "" (:value (:game (:cookies source))))))
                          (:value (:game (:cookies source)))
                          nil)]
    (if (nil? game-cookie-str) nil (keyify (json/read-str game-cookie-str)))))

(defn handle-game-create []
  (if (has-game-setup-params?)
    (assoc
      (redirect "/game/play")
      :cookies (new-game-cookie))
    (redirect "/")))

(defn handle-game-move []
  (if-let [game-cookie (get-game-cookie *request*)]
    (do
      (let [game (get-game-of-uri-value (:game-type game-cookie))
            board-state (convert-string-to-board-state (:board-str game-cookie))
            player-str-coll {:X (:x-player game-cookie) :O (:o-player game-cookie)}
            player-str (get player-str-coll (next-possible-mark game board-state))
            player (get-player-of-uri-value player-str)
            move (if-not (nil? (:choice (:params *request*)))
                   (Integer/parseInt (:choice (:params *request*)))
                   (move player game board-state))
            new-board-state (set-mark-at-index game board-state move)
            new-board-str (convert-board-state-to-string new-board-state)]
        (assoc
          (redirect "/game/play")
          :cookies (apply
                     game-cookie-map
                     (vals (assoc
                             game-cookie
                             :board-str new-board-str))))))
    (redirect "/")))

(defn handle-game-play []
  (if-let [game-cookie (get-game-cookie *request*)]
    (let [game (get-game-of-uri-value (:game-type game-cookie))
          board-state (convert-string-to-board-state (:board-str game-cookie))
          gameover? (gameover? game board-state)
          winner (winner game board-state)
          player-str-coll {:X (:x-player game-cookie) :O (:o-player game-cookie)}
          next-mark (next-possible-mark game board-state)
          player-str (get player-str-coll next-mark)
          meta-refresh (if (and (not gameover?) (not= "human-player" player-str)) {:seconds 1 :url "/game/move"} nil)
          interactive-move? (if (and (not gameover?) (nil? meta-refresh)) true false)
          winning-message (if (and gameover? winner)
                            (str "Game over, " (name winner) " has won.")
                            (if gameover?
                              (str "Game over, tied game")
                              nil))]
      (render-template "game/index" game-cookie
                                    :meta-refresh meta-refresh
                                    :interactive-move? interactive-move?
                                    :gameover? gameover?
                                    :winner winner
                                    :x-player (:x-player game-cookie)
                                    :o-player (:o-player game-cookie)
                                    :next-mark (if-not gameover? next-mark nil)
                                    :winning-message winning-message))
    (redirect "/")))

(defn handle-game-reset []
  (assoc
    (redirect "/")
    :cookies {:game {:value ""}}))

(defroutes game-controller
  (POST "/game/create" [] (handle-game-create))
  (GET  "/game/move"   [] (handle-game-move))
  (GET  "/game/play"   [] (handle-game-play))
  (GET  "/game/reset"  [] (handle-game-reset)))
