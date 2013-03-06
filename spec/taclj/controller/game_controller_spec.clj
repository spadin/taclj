(ns taclj.controller.game-controller-spec
  (:require [speclj.core :refer [describe around it should=
                                 run-specs context should]]
            [joodo.spec-helpers.controller :refer [do-get rendered-template rendered-context
                                                   with-mock-rendering with-routes should-redirect-to]]
            [taclj.controller.game-controller :refer [game-controller
                                                      get-game-of-uri-value
                                                      get-player-of-uri-value
                                                      convert-string-to-board-state]]
            [ticlj.game][ticlj.player]))

(describe "game-controller"
  (with-mock-rendering)
  (with-routes game-controller)

  (it "redirects to the root page page if game type and players are not specified"
    (should-redirect-to (do-get "/game") "/"))

  (it "redirects to game page when all params validate"
    (should-redirect-to (do-get "/game" :params {:game-type "three-by-three-game"
                                                 :x-player "easy-ai-player"
                                                 :o-player "easy-ai-player"})
                         "/game/three-by-three-game/easy-ai-player/easy-ai-player/show?board-str=---------"))

  (it "displays game page"
    (let [response (do-get "/game/three-by-three/easy-ai/medium-ai/show")]
      (should= 200 (:status response))))

  (it "redirects back to the game page after visiting the move page"
    (should-redirect-to (do-get "/game/three-by-three-game/human-player/human-player/move"
                                :params {:board-str "---------"
                                         :choice "0"})
                        "/game/three-by-three-game/human-player/human-player/show?board-str=X--------"))

  (context "/get-game-of-uri-value"
    (it "returns an instance of a game"
      (should (instance? ticlj.game.protocol.Game
                         (get-game-of-uri-value "three-by-three-game")))))

  (context "/get-player-of-uri-value"
    (it "returns an instance of a player"
      (should (instance? ticlj.player.protocol.Player
                         (get-player-of-uri-value "human-player")))))

  (it "converts a string of an empty board to a board-state"
    (should= [:# :# :# :# :# :# :# :# :#]
             (convert-string-to-board-state "---------")))

  (it "converts a second string board to a board-state"
    (should= [:X :O :# :# :# :# :# :# :#]
             (convert-string-to-board-state "XO-------"))))
