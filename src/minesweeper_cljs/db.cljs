(ns minesweeper-cljs.db
  (:require [minesweeper-cljs.game :as game]))

(def default-db
  {:game-state (game/get-initial-state :medium)})
