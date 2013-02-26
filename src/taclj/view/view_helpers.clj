(ns taclj.view.view-helpers
  "Put helper functions for views in this namespace."
  (:use
    [joodo.views :only (render-partial *view-context*)]
    [hiccup.page]
    [hiccup.form]))

(defn get-game-type []
  (:game-type *view-context*))

(defn get-game-types []
  [{:name "3 x 3"
    :uri-value "three-by-three"}
   {:name "4 x 4"
    :uri-value "four-by-four"}])

(defn get-player-types []
  [{:name "Human"
    :uri-value "human"}
   {:name "UnbeatableAI"
    :uri-value "unbeatable-ai"}
   {:name "MediumAI"
    :uri-value "medium-ai"}
   {:name "EasyAI"
    :uri-value "easy-ai"}])

(defn has-message? []
  (not (empty? (:message *view-context*))))

(defn get-message []
  (:message *view-context*))
