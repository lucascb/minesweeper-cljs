(ns minesweeper-cljs.game)

(defn get-random-mines-pos
  [grid-size number-of-mines]
  (let [all-pos (for [i (range grid-size)]
                  (for [j (range grid-size)]
                    [i j]))]
    (->> all-pos
         (mapcat identity)
         shuffle
         (take number-of-mines)
         set)))

(defn valid-pos?
  [grid-size [x y]]
  (and (<= 0 x (dec grid-size)) (<= 0 y (dec grid-size)))) 

(defn get-neighbors-of-pos
  [grid-size [x y]]
  (->> [[(inc x) y]
        [x (inc y)]
        [(dec x) y]
        [x (dec y)]
        [(inc x) (inc y)]
        [(dec x) (dec y)]
        [(inc x) (dec y)]
        [(dec x) (inc y)]]
       (filter #(valid-pos? grid-size %))))

(defn get-number-of-neighbor-mines
  [grid-size mines pos]
  (reduce + (map #(get {true 1 false 0} (contains? mines %))
                 (get-neighbors-of-pos grid-size pos))))

(defn get-game-grid
  [grid-size mines]
  (vec (for [i (range grid-size)]
         (vec (for [j (range grid-size)]
                (if (contains? mines [i j])
                  :mine
                  (get-number-of-neighbor-mines grid-size mines [i j])))))))

(defn empty-pos?
  [grid [i j]]
  (= (nth (nth grid i) j) 0))

(defn reveal-empty-pos-and-neighbors!
  [grid revealed* pos]
  (when (not (contains? @revealed* pos))
    (swap! revealed* #(conj % pos))
    (when (empty-pos? grid pos)
      (doseq [neighbor (get-neighbors-of-pos (count grid) pos)]
        (reveal-empty-pos-and-neighbors! grid revealed* neighbor)))))

(defn reveal-pos
  [grid revealed pos]
  (if (not (empty-pos? grid pos))
    (conj revealed pos) 
    ; Visit all empty neighbors and keep track of positions already visited on a state 
    ; to enhance performance
    (let [revealed* (atom revealed)]
      (reveal-empty-pos-and-neighbors! grid revealed* pos)
      @revealed*)))

(defn select-pos
  [current-state pos]
  (println "select-pos" current-state pos)
  (let [{revealed :revealed
         mines :mines
         grid :grid
         flags :flags} current-state
        revealed' (reveal-pos grid revealed pos)
        flags' (clojure.set/difference flags revealed')]
    (assoc current-state
           :revealed revealed'
           :flags flags'
           :remaining-flags (- (count mines) (count flags'))
           :game-over (contains? mines pos))))

(defn flag-pos
  [current-state pos]
  (println "flag-pos" current-state pos)
  (let [{mines :mines
         flags :flags
         remaining-flags :remaining-flags} current-state] 
    (cond
      (zero? remaining-flags) current-state
      (contains? flags pos) (assoc current-state
                                   :flags (->> flags (remove #(= % pos)) set)
                                   :remaining-flags (inc remaining-flags))
      :else (let [flags' (conj flags pos)
                  won? (= mines flags')]
              (assoc current-state
                     :flags flags'
                     :remaining-flags (dec remaining-flags)
                     :game-over won?
                     :won won?)))))

(defn get-initial-state
  [grid-size number-of-mines]
  (let [mines (get-random-mines-pos grid-size number-of-mines)]
    {:grid-size grid-size
     :number-of-mines number-of-mines
     :remaining-flags number-of-mines
     :mines mines
     :grid (get-game-grid grid-size mines)
     :flags #{}
     :revealed #{}
     :game-over false
     :won false}))