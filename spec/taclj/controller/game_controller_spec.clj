(ns taclj.controller.game-controller-spec
  (:require [speclj.core :refer [with describe around it should= should-not=
                                 run-specs context should should-contain should-not-contain]]
            [joodo.middleware.request :refer (*request*)]
            [joodo.spec-helpers.controller :refer [do-get do-post rendered-template rendered-context
                                                   with-mock-rendering with-routes should-redirect-to]]
            [clojure.data.json :as json]
            [taclj.controller.game-controller :refer [game-controller
                                                      get-game-of-uri-value
                                                      get-player-of-uri-value
                                                      convert-string-to-board-state
                                                      new-game-cookie game-cookie-map
                                                      get-game-cookie]]
            [ticlj.game][ticlj.player]))

;; ## Helper functions
;;
;; Converts map and vector keys to keywords
(defn keyify [target]
  (if (map? target)
    (into {}
      (for [[k v] target]
        [(keyword k) (keyify v)]))
    (if (vector? target)
      (vec (map keyify target))
      target)))
;;
;; Asserts a cookie keyword map contains the correct keys
(defn test-game-cookie-is-set [obj]
  (let [game (keyify (json/read-str (:value (:game obj))))]
    (should-contain :game obj)
    (should-contain :game-type game)
    (should-contain :x-player  game)
    (should-contain :o-player  game)
    (should-contain :board-str game)))

(describe "game-controller"
  (with-mock-rendering)
  (with-routes game-controller)
  (with new-game-params {:game-type "three-by-three-game"
                         :x-player "human-player"
                         :o-player "human-player"})

  (with sample-game-cookie (game-cookie-map "three-by-three-game"
                                         "human-player"
                                         "human-player"
                                         "---------"))

  (context "#get-game-of-uri-value"
    (it "returns an instance of a game"
      (should (instance? ticlj.game.protocol.Game
                         (get-game-of-uri-value "three-by-three-game")))))

  (context "#get-player-of-uri-value"
    (it "returns an instance of a player"
      (should (instance? ticlj.player.protocol.Player
                         (get-player-of-uri-value "human-player")))))

  (context "#new-game-cookie"
    (it "returns a map for a new game cookie"
      (binding [*request* {:params @new-game-params}]
        (test-game-cookie-is-set (new-game-cookie)))))

  (context "#convert-string-to-board-state"
    (it "converts a string of an empty board to a board-state"
      (should= [:# :# :# :# :# :# :# :# :#]
               (convert-string-to-board-state "---------")))

    (it "converts a second string board to a board-state"
      (should= [:X :O :# :# :# :# :# :# :#]
               (convert-string-to-board-state "XO-------"))))

  (context "url: /game/reset"
    (it "redirects to root"
      (should-redirect-to (do-get "/game/reset")
                          "/"))

    (it "returns an empty game cookie in the response"
      (let [response (do-get "/game/reset")
            cookies (:cookies response)]
        (should-not= nil cookies)
        (should= "" (:value (:game cookies))))))

  (context "url: /game/create"
    (it "returns a new game cookie in the response"
      (let [response (do-post "/game/create" :params @new-game-params)]
        (test-game-cookie-is-set (:cookies response))))

    (it "redirects to the play page"
      (should-redirect-to (do-post "/game/create" :params @new-game-params)
                          "/game/play")))

  (context "url: /game/play"
    (it "redirects to root when game cookie is not present"
      (should-redirect-to (do-get "/game/play")
                          "/"))

    (it "responds with 200 if a proper game cookie is present"
      (binding [*request* {:params @new-game-params}]
        (let [response (do-get "/game/play" :cookies (new-game-cookie))]
          (should= 200 (:status response))))))

  (context "url: /game/move"
    (it "redirects to root if there is not game cookie"
      (should-redirect-to (do-get "/game/move")
                          "/"))

    (it "redirects to the play page"
      (should-redirect-to (do-get "/game/move" :cookies @sample-game-cookie
                                               :params {:choice "0"})
                          "/game/play"))

    (it "updates the game cookie with a new board"
      (let [response (do-get "/game/move" :cookies @sample-game-cookie
                                          :params {:choice "0"})
            game-cookie (get-game-cookie response)]
        (should-not= game-cookie nil)
        (should= "X--------"
                 (:board-str game-cookie))))))
