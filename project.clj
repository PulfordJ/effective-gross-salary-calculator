(defproject effective-gross-salary-calculator "1.0.3"
  :description "This calculator allows you to add various aspects of salary such as; base-salary, pro-rataed bonus and bonus that vests on a given date to calculate an effective gross salary over a given period of time."
  :url "https://github.com/PulfordJ/effective-gross-salary-calculator"
  :license {:name "Apache Licence 2.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"][clj-time "0.13.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}  
  :plugins [
            ;Development plugins
            [lein-auto "0.1.3"]
            ;[lein-cljfmt "0.5.7"]
            [lein-bump-version "0.1.6"]
            ])
