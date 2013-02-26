(ns taclj.view.view-helpers-spec
  (:require [speclj.core :refer [describe it should= run-specs around]]
            [taclj.view.view-helpers :refer [get-game-types get-player-types]]))

(describe "view_helpers"
  (it "gets the possible game types"
    (should= [{:name "3 x 3"
               :uri-value "three-by-three"}
              {:name "4 x 4"
               :uri-value "four-by-four"}]
             (get-game-types)))

  (it "gets the possible player types"
    (should= [{:name "Human"
               :uri-value "human"}
              {:name "UnbeatableAI"
               :uri-value "unbeatable-ai"}
              {:name "MediumAI"
               :uri-value "medium-ai"}
              {:name "EasyAI"
               :uri-value "easy-ai"}]
             (get-player-types))))
