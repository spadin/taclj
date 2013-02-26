[:h1 "Taclj"
  [:em [:small "- tic tac toe"]]]

[:h3 "Start a new game"]

[:form {:action "/game" :id "new-game"}
  [:label {:for "game-type"} "Choose game type"]
  [:select {:name "game-type" :id "game-type"}
    (for [game-type (get-game-types)]
      [:option {:value (:uri-value game-type)} (:name game-type)])]

  [:label {:for "x-player"} "X Player Type"]
  [:select {:name "x-player" :id "x-player"}
    (for [player-type (get-player-types)]
      [:option {:value (:uri-value player-type)} (:name player-type)])]

  [:label {:for "o-player"} "O Player Type"]
  [:select {:name "o-player" :id "o-player"}
    (for [player-type (get-player-types)]
      [:option {:value (:uri-value player-type)} (:name player-type)])]

  [:input {:type "submit"
           :value "Start game"
           :class "submit"}]]
