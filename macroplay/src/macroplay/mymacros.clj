(ns macroplay.mymacros)

(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(defmacro infix
  [infix-call]
  (seq(conj (vec (rest infix-call)) (first infix-call))))

(defmacro infix-1
  [[operand1 op operand2]]
  (list op operand1 operand2))

(defmacro my-println
  [result]
  (list
   'let
   ['res 'result]
   (list 'println 'res)
   'res))

(defn code-criticise
  [msg code]
  `(println ~msg (quote ~code)))

(defmacro code-critic
  [bad-code good-code]
  `(do ~(code-criticise "This is bad" bad-code)
	   ~(code-criticise "This is good" good-code)))

(defmacro code-critic-1
  [bad good]
  `(do ~@(map #(apply code-criticise %)
			  [["This is bad" bad]
			   ["This is good" good]])))

(defmacro without-mischief
  [& stuff-to-do]
  (let [mmsg (gensym 'message)]
  `(let [~mmsg "Oh, big deal!!"]
	 ~@stuff-to-do
	 (println "I still want to say:" ~mmsg))))

(defmacro with-report
  [to-try]
  `(let [result# ~to-try]
	 (if result#
	   (println (quote ~to-try) "was successful:" result#)
	   (println (quote ~to-try) "was failed:" result#))))
