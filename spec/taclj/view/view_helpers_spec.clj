(ns taclj.view.view-helpers-spec
  (:use [taclj.view.view-helpers]
        [speclj.core]
        [joodo.views :only (*view-context*)]
        [joodo.middleware.request :only (*request*)]))

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
    (binding [*view-context* {:game-type "three-by-three-game"}]
      (should= "three-by-three-game" (get-game-type))))

  (it "returns the game board based on params"
    (binding [*view-context* {:board-str "X--------"}]
      (should= "X--------"
               (get-board-str))))

  (it "returns the board partial name"
    (should= "game/three_by_three_game_board"
             (get-board-partial "three-by-three-game")))

  (it "converts dashes to underscores"
    (should= "sample_string_cool_stuff"
             (dashes-to-underscore "sample-string-cool-stuff")))

  (it "returns the state when it is a move"
    (should= [:span "X"]
             (state-or-move-link 0 "X")))

  (it "returns a link when the state is not a move"
    (should-contain :a
                    (state-or-move-link 0 "-"))))
