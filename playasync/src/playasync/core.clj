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

(defn thread-demo [name]
  (let [t (thread (do
					(Thread/sleep 1000)
					name))]
	(println "Returned from thread:" (<!! t))))

(defn hot-dog-machine []
  (let [in (chan)
		out (chan)]
	(a/go-loop []
		(let [money (<! in)]
		  (println "Money received:" money)
		  (>! out "hot dog!!"))
	  (recur))
	[in out]))

(defn buy-hot-dog
  [how-many]
  (let [[in out] (hot-dog-machine)]
	(loop [num how-many]
	  (if (> num 0)
		(do
		  (println "Buying...")
		  (>!! in "1 dollar")
		  (println "I got a" (<!! out))
		  (recur (dec num)))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
