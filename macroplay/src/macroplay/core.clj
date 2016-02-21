(ns macroplay.core
  (:require [macroplay.mymacros :refer :all])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
   (+ 1 2 3))
  (println (ignore-last-operand (+ 1 2 3))))
