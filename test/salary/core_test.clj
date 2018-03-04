(ns salary.core-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [salary.core :refer :all]))

(def salaries-timetable
  [
   [12000 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   [24000 
    (t/date-time 2018 1) 
    (t/date-time 2019 1)]   
   ]
  )

(def bonuses-timetable
  [
   [1200 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   [2400 
    (t/date-time 2018 1) 
    (t/date-time 2019 1)]   
   ]
  )

(def salaries-with_prorata_bonustimetable
  [
   ;A typical salary
   [12000 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   ;pro-rataed bonus, behaves a like normal salary.
   [6000 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   ]
  )
(def bonuses-halfway-through-year-timetable
  [
   ;Stock -> wait until end date or lose everything.
   [1200 
    (t/date-time 2017 7) 
    (t/date-time 2018 7)]   
   ]
  )

(deftest salary-test 
  (testing "effective-bonus should be 11000 when 11 out of 12 months have been worked for 12000 bonus."
    (is (= (effective-bonus 12000 (t/date-time 2018 1) (t/date-time 2019 1) (t/date-time 2018 1) (t/date-time 2018 12)) 11000)))

  (testing "monthly-window should successfully calculate salary over two years."
    (is (= (monthly-window salaries-timetable bonuses-timetable (t/date-time 2017 1) 13) 
           [
            [[2017 1] {:base-salary 12000, :bonus 1200, :total 13200, :total-per-month 1100, :months 12, :effective-salary-for-year 13200}]
            [[2017 2] {:base-salary 13000, :bonus 1400, :total 14400, :total-per-month 1200, :months 12, :effective-salary-for-year 14400}]
            [[2017 3] {:base-salary 14000, :bonus 1600, :total 15600, :total-per-month 1300, :months 12, :effective-salary-for-year 15600}]
            [[2017 4] {:base-salary 15000, :bonus 1800, :total 16800, :total-per-month 1400, :months 12, :effective-salary-for-year 16800}]
            [[2017 5] {:base-salary 16000, :bonus 2000, :total 18000, :total-per-month 1500, :months 12, :effective-salary-for-year 18000}]
            [[2017 6] {:base-salary 17000, :bonus 2200, :total 19200, :total-per-month 1600, :months 12, :effective-salary-for-year 19200}]
            [[2017 7] {:base-salary 18000, :bonus 2400, :total 20400, :total-per-month 1700, :months 12, :effective-salary-for-year 20400}]
            [[2017 8] {:base-salary 19000, :bonus 2600, :total 21600, :total-per-month 1800, :months 12, :effective-salary-for-year 21600}]
            [[2017 9] {:base-salary 20000, :bonus 2800, :total 22800, :total-per-month 1900, :months 12, :effective-salary-for-year 22800}]
            [[2017 10] {:base-salary 21000, :bonus 3000, :total 24000, :total-per-month 2000, :months 12, :effective-salary-for-year 24000}]
            [[2017 11] {:base-salary 22000, :bonus 3200, :total 25200, :total-per-month 2100, :months 12, :effective-salary-for-year 25200}]
            [[2017 12] {:base-salary 23000, :bonus 3400, :total 26400, :total-per-month 2200, :months 12, :effective-salary-for-year 26400}]
            [[2018 1] {:base-salary 24000, :bonus 3600, :total 27600, :total-per-month 2300, :months 12, :effective-salary-for-year 27600}]])))

  (testing "monthly-window first element should be same as year-stat with same parameters."
    (is (= (first (monthly-window salaries-timetable bonuses-timetable (t/date-time 2017 1) 12)) [[2017 1](year-stats salaries-timetable bonuses-timetable (t/date-time 2017 1))])))

  (testing "year-stats for january 2017"
    (is (= (year-stats salaries-timetable bonuses-timetable (t/date-time 2017 1)) {:base-salary 12000, :bonus 1200, :total 13200, :total-per-month 1100, :months 12, :effective-salary-for-year 13200}))
    )

  (testing "year-stats for january 2018"
    (is (= (year-stats salaries-timetable bonuses-timetable (t/date-time 2018 1)) {:base-salary 24000, :bonus 3600, :total 27600, :total-per-month 2300, :months 12, :effective-salary-for-year 27600}))
    )
  (testing "year-stats for january 2018 with bonus that runs July 2017-2018"
    (is (= (year-stats salaries-timetable bonuses-halfway-through-year-timetable (t/date-time 2017 1)) {:base-salary 12000, :bonus 600, :total 12600, :total-per-month 1050, :months 12, :effective-salary-for-year 12600} ))
    )

  (testing "total-base-salary"
    (is (= (total-base-salary salaries-timetable (t/date-time 2017 1)(t/date-time 2018 1)) 12000))
    )

  (testing "effective-salary"
    (is (= (effective-salary 12000 (t/date-time 2017 1) (t/date-time 2018 1) (t/date-time 2017 1) (t/date-time 2018 1)) 12000))
    )
  )
