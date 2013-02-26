[:div {:class "board-container"}
  (if (= (:game-type *view-context*) "three-by-three")
    (do
      [:ul {:class (str "board board-type-" (:game-type *view-context*))}
        (for [i (range 9)]
          [:li ""])])
    (do
      [:ul {:class (str "board board-type-" (:game-type *view-context*))}
        (for [i (range 16)]
          [:li ""])]))]
