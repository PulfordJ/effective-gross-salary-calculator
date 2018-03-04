[![Clojars Project](https://img.shields.io/clojars/v/effective-gross-salary-calculator.svg)](https://clojars.org/effective-gross-salary-calculator)
[![Build Status](https://travis-ci.org/PulfordJ/effective-gross-salary-calculator.svg?branch=master)](https://travis-ci.org/PulfordJ/effective-gross-salary-calculator)

# effective-gross-salary-calculator

This calculator allows you to add various aspects of salary such as; base-salary, pro-rataed bonus and typical bonus that vests on a given date to calculate an effective gross salary over a given period of time.

#Example usage
```clojure
(def salaries-timetable
  [
   ;A typical salary
   [12000 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   ;pro-rataed sign-on, behaves a like normal salary.
   [6000 
    (t/date-time 2017 1) 
    (t/date-time 2018 1)]   
   ]
  )

(def bonuses-timetable
  [
   ;Stock -> wait until end date or lose everything.
   [1200 
    (t/date-time 2017 7) 
    (t/date-time 2018 7)]   
   ]
  )

(year-stats salaries-timetable bonuses-halfway-through-year-timetable (t/date-time 2017 1)) 
```

Will get you:

```clojure
{:base-salary 18000,
 :bonus 600,
 :total 18600,
 :total-per-month 1550,
 :months 12,
 :effective-salary-for-year 18600}
```
See tests for more examples.

Note that the calculation includes the value of the bonus accumulated even though it's only acquired if you are employed till the end date. This is to track the fact that the closer you are to a given bonus vesting the more valuable your "month" is and the further away the less valuable.

To take an example, imagine a bonus that would vest in december at £12,000 and you are in November. To ignore the bonus when calculating the value of November would be highly questionable, but to ignore it in January would be less so; £12,000 next month is more valuable than £12,000 in 12 months.
## Installation

Follow the instructions found here: https://clojars.org/effective-gross-salary-calculator

## License

Copyright © 2018
See LICENSE file for more details
