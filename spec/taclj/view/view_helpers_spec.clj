(ns taclj.view.view-helpers-spec
  (:use [taclj.view.view-helpers]
        [speclj.core]
        [joodo.views :only (*view-context*)]))

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
             (get-player-types)))

  (it "determines if there are any messages"
    (should-not (has-message?))
    (binding [*view-context* {:message "hello there"}]
      (should (has-message?))))

  (it "returns the message"
    (should= nil (get-message))
    (binding [*view-context* {:message "hello there"}]
      (should= "hello there" (get-message))))

  (it "returns nil game type when not set"
    (should= nil (get-game-type)))

  (it "returns the game type"
    (binding [*view-context* {:game-type "three-by-three"}]
      (should= "three-by-three" (get-game-type)))))
