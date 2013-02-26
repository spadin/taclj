[:h1 "Taclj"
  [:em [:small "- tic tac toe"]]]

[:h3 "Start a new game"]

[:form {:action "/game" :id "new-game"}
  [:label {:for "game-type"} "Choose game type"]
  [:select {:name "game-type" :id "game-type"}
    [:option {:value "three-by-three"} "3 x 3"]
    [:option {:value "four-by-four"} "4 x 4"]]

  [:label {:for "x-player"} "X Player Type"]
  [:select {:name "x-player" :id "x-player"}
    [:option {:value "Human"} "Human"]
    [:option {:value "UnbeatableAI"} "UnbeatableAI"]
    [:option {:value "MediumAI"} "MediumAI"]
    [:option {:value "EasyAI"} "EasyAI"]]

  [:label {:for "o-player"} "O Player Type"]
  [:select {:name "o-player" :id "o-player"}
    [:option {:value "Human"} "Human"]
    [:option {:value "UnbeatableAI"} "UnbeatableAI"]
    [:option {:value "MediumAI"} "MediumAI"]
    [:option {:value "EasyAI"} "EasyAI"]]

  [:input {:type "submit"
           :value "Start game"
           :class "submit"}]]
