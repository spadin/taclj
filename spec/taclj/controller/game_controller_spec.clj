(ns taclj.controller.game-controller-spec
  (:require [speclj.core :refer [describe around it should= run-specs]]
            [joodo.spec-helpers.controller :refer [do-get rendered-template rendered-context
                                                   with-mock-rendering with-routes should-redirect-to]]
            [taclj.controller.game-controller :refer [game-controller]]))

(describe "game-controller"
  (with-mock-rendering)
  (with-routes game-controller)

  (it "redirects to the root page page if game type and players are not specified"
    (should-redirect-to (do-get "/game") "/"))

  (it "redirects to game page when all params validate"
    (should-redirect-to (do-get "/game" :params {:game-type "three-by-three"
                                                 :x-player "easy-ai"
                                                 :o-player "easy-ai"})
                         "/game/three-by-three/easy-ai/easy-ai/play"))

  (it "displays game page"
    (let [response (do-get "/game/three-by-three/easy-ai/medium-ai/play")]
      (should= 200 (:status response)))))
