(ns playasync.core
  (:require [clojure.core.async :as a :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]])
  (:gen-class))


(defn echo-chan-demo []
  (let [echo-chan (chan)]
	(go (println (<! echo-chan)))
	(>!! echo-chan "Ketchup")))

(defn echo-chan-with-buf-demo []
  (let [echo-buf (chan 2)]
	(go (do (Thread/sleep 5000)
			(println (<! echo-buf))))
	(>!! echo-buf "Hey");Not blocking
	(println "Hey Sent")
	(>!! echo-buf "Hey");Not blocking
	(println "Hey Sent")
	(>!! echo-buf "Hey");blocking until the process picks the first "Hey"
	(println "Hey Sent")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
