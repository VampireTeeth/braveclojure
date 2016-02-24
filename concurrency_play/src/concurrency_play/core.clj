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

(def yak-butter-international
  {:store "Yak Butter International"
    :price 90
    :smoothness 90})

(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})

;; This is the butter that meets our requirements
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(def all-butters [yak-butter-international butter-than-nothing baby-got-yak])

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  "If the butter meets our criteria, return the butter, else return false"
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

(defn find-the-best-butter []
  (let [res-promise (promise)]
	(doseq [b all-butters]
	  (future
		(if-let [satisfactory-butter (satisfactory? (mock-api-call b))]
		  (deliver res-promise satisfactory-butter))))
	res-promise))

(defmacro wait
  [timeout & body]
  `(do (Thread/sleep ~timeout) ~@body))

(defmacro enqueue
  ([q current-promise concurrent serialized]
   `(let [~current-promise (promise)]
	  (future (deliver ~current-promise ~concurrent))
	  (deref ~q)
	  ~serialized
	  ~current-promise))
  ([current-promise concurrent serialized]
  `(enqueue (future) ~current-promise ~concurrent ~serialized)))


(defmacro enqueue-with-fn
  ([q current-promise concurrent f]
   `(let [~current-promise (promise)]
	  (future (deliver ~current-promise ~concurrent))
	  (deref ~q)
	  (apply  ~f [(deref ~current-promise)])
	  ~current-promise))
  ([current-promise concurrent f]
  `(enqueue-with-fn (future) ~current-promise ~concurrent ~f)))

(defn enqueue-demo []
  (time
   (->
	(enqueue saying (wait 2000 "Eidka") (println @saying))
	(enqueue saying (wait 4000 "Ajsdksad") (println @saying))
	(enqueue saying (wait 1000 "vcmvedzu") (println @saying)))))

(defn enqueue-with-fn-demo []
  (time
   (->
	(enqueue-with-fn saying (wait 2000 "Eidka") #(println %))
	(enqueue-with-fn saying (wait 4000 "Ajsdksad") #(println %))
	(enqueue-with-fn saying (wait 1000 "vcmvedzu") #(println %)))))


(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0)
	   (<= percent-deteriorated 100)))

(def fred
  (atom {:cuddle-hunger-level 0
		 :cuddle-decay-level 1
		 :percent-deteriorated 0}
  :validator percent-deteriorated-validator))

(defn shuffle-speed [zombie]
  (* (:cuddle-hunger-level zombie)
	 (- 100 (:percent-deteriorated zombie))))

(defn shuffle-alert
  [key watched old-state new-state]
  (let [sph (shuffle-speed new-state)]
	(if (> sph 5000)
	  (do
		(println "Fuck!!!! RUN!!!" sph)
		(println "Key" key))
	  (do
		(println "It is still fine now!" sph)
		(println "Key" key)))))

(defn atom-play-1 []
  (swap! fred (fn [current-state]
				(merge-with + current-state {:cuddle-hunger-level 1
									:cuddle-decay-level 1}))))

(defn increase-cuddle-hunger-level
  [zombie-state increase-by]
  (merge-with + zombie-state {:cuddle-hunger-level increase-by}))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (let [result (my-future)]
	(println "Result: " (deref result))
	(println "Result: " @result)))
