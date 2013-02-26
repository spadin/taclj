(if (has-message?)
  [:p (get-message)])

[:div {:class "board-container"}
  (if (= (get-game-type) "three-by-three")
      [:ul {:class (str "board board-type-" (get-game-type))}
        (for [i (range 9)]
          [:li ""])]
      [:ul {:class (str "board board-type-" (get-game-type))}
        (for [i (range 16)]
          [:li ""])])]
