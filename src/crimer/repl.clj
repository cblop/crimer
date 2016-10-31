(ns crimer.repl
  (:require [faker.name :as name]
            [faker.lorem :as lorem]
            [faker.address :as address]
            [faker.phone-number :as phone]
            [clojure.java.jdbc :as j]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all]))

(def killer
  {:name "Mac McLaren"
   :age 22
   :gender "m"
   :height 186
   :weight 65
   :address "34 Charleston Avenue"
   :city "Bristol"
   :phone "0345 324 233"})

(def suspect-a
  {:name "Beth Anderson"
   :age 34
   :gender "f"
   :height 153
   :weight 45
   :address "32 Oak Street"
   :city "Bath"
   :phone "0345 634 335"})

(def suspect-b
  {:name "Andy Farmer"
   :age 86
   :gender "m"
   :height 198
   :weight 150
   :address "83 Mulholland Drive"
   :city "Weston-Super-Mare"
   :phone "0245 434 555"})

(def suspect-c
  {:name "Lee Scott"
   :age 26
   :gender "m"
   :height 185
   :weight 65
   :address "2 Kipling Avenue"
   :city "Bristol"
   :phone "0342 345 322"})

(def witness-a
  {:name "Gordon Knot"
   :age 21
   :gender "m"
   :height 160
   :weight 100
   :address "18 Pall Mall Drive"
   :city "Bath"
   :phone "0345 323 333"})

(def witness-b
  {:name "Michelle Brewer"
   :age 64
   :gender "f"
   :height 145
   :weight 40
   :address "99 Aces Avenue"
   :city "Bath"
   :phone "0345 399 994"})

(def interview-a
  {:interview "I saw a skinny-looking man get into a black Honda just after the murder happened. Then he went speeding off!"
   :name "Gordon Knot"})

(def interview-b
  {:interview "Yes, I saw someone mixing up some blue chemicals. It was a guy, maybe 21 or 22 years old."
   :name "Michelle Brewer"})

(def interview-c
  {:interview "Hmm, well, I saw a shady character messing around with the victim's food. Must have been at least 180cm tall."
   :name "Beth Anderson"})

(def interview-d
  {:interview "I saw the license plate: it was 'BSO' - something, something."
   :name "Andy Farmer"})

(def interview-e
  {:interview "I got a call before the murder, warning me that there was going to be a murder. The number started with 0345..."
   :name "Lee Scott"})


(def report-a
  {:report "The victim bought his burrito at the Student Union cafe."
   :location "Student Union cafe"})

(def report-b
  {:report "Beth Anderson, the server at the Student Union cafe, noticed a shady character messing with the victim's food."
   :location "Staff room"})

(def report-c
  {:report "Gordon Knot, a music teacher, saw somebody suspicious just after the murder."
   :location "Music classroom"})

(def report-d
  {:report "Michelle Brewer, who works at the bar, saw something suspicious."
   :location "Student Bar"})

(def report-e
  {:report "Lee Scott, a lecturer, got a call from the killer."
   :location "Lecture room"})

(def vehicle-a
  {:colour "Black"
   :make "Honda"
   :year 1978
   :owner "xxx xxxxren"
   :license "BSO-2342"})

(defn person-gen []
  (let [name (first (name/names))
        gender (rand-nth ["m" "f"])
        age (+ (rand-int 85) 15)
        height (+ (rand-int 100) 100)
        weight (+ (rand-int 40) 100)
        address (str (address/street-address))
        city (rand-nth ["Bath" "Bristol" "Weston-Super-Mare"])
        phone (first (phone/phone-numbers))]
    {:name name
     :age age
     :gender gender
     :height height
     :weight weight
     :address address
     :city city
     :phone phone}))

(defn interview-gen [name]
  {:interview (apply str (take 3 (lorem/paragraphs)))
   :name name})

(defn report-gen []
  {:report (apply str (take 3 (lorem/paragraphs)))
   :location (address/street-name)})

(defn vehicle-gen []
  (let [colour (rand-nth ["Blue" "Red" "Green" "Black" "White" "Yellow" "Pink"])
        make (rand-nth ["Nissan" "Lexus" "Ford" "Volkswagon" "BMW" "Mercedes" "Tesla" "Skoda" "Alfa Romeo" "Jaguar"])
        year (+ 1970 (rand-int 46))
        owner (first (name/names))
        license (str (apply str (take 3 (shuffle (map char (range 65 91)))))
                     "-" (+ 1000 (rand-int 9999))
                     )]
    {:colour colour
     :make make
     :year year
     :owner owner
     :license license}))

(take 10 (repeatedly person-gen))

(defn make-records [num suspects witnesses interviews reports vehicles]
  (let [rand-people (take num (repeatedly person-gen))
        rand-interviews (take num (map interview-gen (map :name rand-people)))
        rand-reports (take num (repeatedly report-gen))
        rand-vehicles (take num (repeatedly vehicle-gen))
        ]
    {:suspects (shuffle (concat suspects witnesses rand-people))
     :interviews (shuffle (concat interviews rand-interviews))
     :reports (shuffle (concat reports rand-reports))
     :vehicles (shuffle (concat vehicles rand-vehicles))}
    ))

(def db (make-records 1000 [killer suspect-a suspect-b suspect-c] [witness-a witness-b] [interview-a interview-b interview-c interview-d interview-e] [report-a report-b report-c report-d] [vehicle-a]))

;; (:interviews db)

;; (:people (make-records [killer suspect-a suspect-b suspect-c] [witness-a witness-b] [] [] []))
