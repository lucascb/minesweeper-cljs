(ns minesweeper-cljs.events
  (:require
   [re-frame.core :as re-frame]
   [minesweeper-cljs.db :as db]
   [minesweeper-cljs.game :as game]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _] db/default-db))

(re-frame/reg-event-db
 :select-pos
 (fn [db [_ pos]] (assoc db :game-state (game/select-pos (:game-state db) pos))))

(re-frame/reg-event-db
 :flag-pos
 (fn [db [_ pos]] (assoc db :game-state (game/flag-pos (:game-state db) pos))))

(re-frame/reg-event-db
 :reset-db
 (fn [db []] (assoc db :game-state (game/get-initial-state 9 9))))