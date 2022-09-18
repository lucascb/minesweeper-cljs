(ns minesweeper-cljs.views
  (:require
   [re-frame.core :as re-frame]
   [minesweeper-cljs.subs :as subs]))

(defn convert-col
  [col]
  (if (= col :mine) [:img {:src "mine.png"}] col))

(defn table-column
  [revealed flags game-over? i j col]
  ^{:key (str i j)}
  [:td [:button
        {:class "grid-button"
         :on-click #(re-frame/dispatch [:select-pos [i j]])
         :on-context-menu (fn [e]
                            (.preventDefault e)
                            (re-frame/dispatch [:flag-pos [i j]]))
         :disabled game-over?}
        (cond (contains? revealed [i j]) (convert-col col)
              (contains? flags [i j]) [:img {:src "flag.png"}]
              :else "")]])

(defn table-row
  [revealed flags game-over? i row]
  ^{:key i} [:tr (map-indexed #(table-column revealed flags game-over? i %1 %2) row)])

(defn game-table
  [grid revealed flags game-over?]
  [:table [:tbody (map-indexed #(table-row revealed flags game-over? %1 %2) grid)]])

(defn game-status
  [game-over? won? remaining-flags]
  [:p (if game-over?
        (if won? "You won! " "You lost!")
        (str remaining-flags " flags remaining"))])

(defn reset-button
  []
  [:div {:id "reset-button"}
   [:button {:on-click #(re-frame/dispatch [:reset-db])} "Reset"]])

(defn main-panel []
  (let [{grid :grid
         flags :flags
         revealed :revealed
         remaining-flags :remaining-flags
         game-over? :game-over
         won? :won} @(re-frame/subscribe [::subs/game-state])]
    [:div {:id "content"}
     [:div [:h1 "Minesweeper"]]
     [game-table grid revealed flags game-over?]
     [game-status game-over? won? remaining-flags]
     [reset-button]
     ]))
