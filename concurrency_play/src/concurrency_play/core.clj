(ns concurrency-play.core
  (:gen-class))



(defn my-future []
  (future
	(Thread/sleep 4000)
	(println (.getName (Thread/currentThread)) "is finished sleeping")
	(+ 1 1)))

(defn my-delay []
  (delay
   (let [msg "Just call my name and I will be there"]
	 (println "First deref:" msg)
	 msg)))

(def my-headshots ["fun.jpg" "serious.jpg" "playful.jpg"])

(defn upload-document [doc]
  (let [thread-name (.getName (Thread/currentThread))]
		(println "Sending document" doc "......")
		(Thread/sleep 4000)
		(println "Done")
		true))

(defn send-email [email-address]
  (println "Sending notification email to" email-address))

(defn upload-and-notify [headshots]
	(let [d (delay (send-email "steven.weike.liu@gmail.com"))]
	  (doseq [headshot headshots]
		(future (upload-document headshot)
				(force d)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (let [result (my-future)]
	(println "Result: " (deref result))
	(println "Result: " @result)))
