(if (has-message?)
  [:p (get-message)])

[:div {:class "board-container"}
  [:ul {:class (str "board board-type-" (get-game-type))}
    (map-indexed
      (fn [idx state]
        [:li (state-or-move-link idx state)])
      (get-board-str))]]

[:a {:href "/" :class "new-game"} "start a new game"]
