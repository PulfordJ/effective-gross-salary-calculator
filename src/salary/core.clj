(ns salary.core
  (:gen-class))
(require '[clj-time.core :as t])
(require '[clojure.pprint :as p])

(defn effective-salary [salary-amount, salary-start-date, salary-end-date, start-date, end-date]
    (if (or 
          (t/before? salary-end-date start-date)
          (t/before? end-date salary-start-date))

      0
      ( * (/ salary-amount 12) (t/in-months (t/interval (if (t/after? salary-start-date start-date) salary-start-date start-date) (if (t/before? salary-end-date end-date) salary-end-date end-date)))
      ) ))

(defn total-base-salary [salaries-timetable start-date end-date] 
  (apply + (map (fn[x] (apply effective-salary (conj x start-date end-date))) salaries-timetable)))

(defn effective-bonus [bonus-amount, bonus-start-date, bonus-end-date, start-date, end-date]
    (if (or 
          (t/before? bonus-end-date start-date)
          (t/before? end-date bonus-start-date))

      0
      ( * (/ bonus-amount (t/in-months (t/interval bonus-start-date bonus-end-date))) (t/in-months (t/interval bonus-start-date (if (t/before? bonus-end-date end-date) bonus-end-date end-date)))
      ) ))

(defn total-bonus [bonuses-timetable start-date end-date]
  (apply + (map (fn[x] (apply effective-bonus (conj x start-date end-date))) bonuses-timetable))
  )

(defn total-compensation [salaries-timetable bonuses-timetable start-date end-date] 
  (+ (total-base-salary salaries-timetable start-date end-date)
     (total-bonus bonuses-timetable start-date end-date))
  )

(defn stats [salaries-timetable bonuses-timetable start-date end-date] (let [total (total-compensation salaries-timetable bonuses-timetable start-date end-date)
                                        months (t/in-months (t/interval start-date end-date))
                                        month-total (/ total months)] 
  {:base-salary (int (total-base-salary salaries-timetable start-date end-date))
   :bonus (int (- total (total-base-salary salaries-timetable start-date end-date)))
   :total (int total)
   :total-per-month (int month-total)
   :months months
   :effective-salary-for-year (int (* month-total 12))
   }))

(defn year-stats [salaries-timetable bonuses-timetable start-date] (stats salaries-timetable bonuses-timetable start-date (t/plus start-date (t/years 1) )))
 
(defn monthly-window [salaries-timetable bonuses-timetable start-date number-of-windows] (vec (for [x (range 0 number-of-windows) :let [arg-date (t/plus start-date (t/months x))]] [[(t/year arg-date) (t/month arg-date)] (year-stats salaries-timetable bonuses-timetable arg-date)])))
