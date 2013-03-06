(ns taclj.view.view-helpers-spec
  (:use [taclj.view.view-helpers]
        [speclj.core]
        [joodo.views :only (*view-context*)]))

(describe "view_helpers"
  (context "/get-game-types"
    (it "contains a name key in the hash map"
      (should-contain :name
                      (first (get-game-types))))

    (it "contains a uri-value key in the hash map"
      (should-contain :uri-value
                      (first (get-game-types)))))

  (context "/get-player-types"
    (it "contains a name key in the hash map"
      (should-contain :name
                      (first (get-player-types))))

    (it "contains a uri-value key in the hash map"
      (should-contain :uri-value
                      (first (get-player-types)))))

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
