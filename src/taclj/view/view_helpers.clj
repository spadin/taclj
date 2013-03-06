(ns taclj.view.view-helpers
  "Put helper functions for views in this namespace."
  (:use
    [joodo.views :only (render-partial *view-context*)]
    [hiccup.page]
    [hiccup.form]
    [ticlj.player]
    [ticlj.game]
    [clojure.string :only [split]]))

(defn get-game-type []
  (:game-type *view-context*))

(defn get-game-types []
  (map (fn [game-type]
         (assoc game-type :uri-value (second (split (:value game-type) #"\/"))))
       available-game-types))

(defn get-player-types []
  (map (fn [player-type]
         (assoc player-type :uri-value (second (split (:value player-type) #"\/"))))
       available-player-types))

(defn has-message? []
  (not (empty? (:message *view-context*))))

(defn get-message []
  (:message *view-context*))
