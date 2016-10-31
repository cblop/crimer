(ns crimer.db
  (:require
   [crimer.repl :as crimes]
   [clojure.java.jdbc :as j]
            [honeysql.core :as sql]
            [honeysql.helpers :refer :all]))

(def db-map {:subprotocol "mysql"
             :subname "//localhost:3306/investigation"
             :user "columbo"
             :password "onemorething"})

(defn drop-tables []
  (do
    (j/db-do-commands db-map (j/drop-table-ddl :report))
    (j/db-do-commands db-map (j/drop-table-ddl :interview))
    (j/db-do-commands db-map (j/drop-table-ddl :suspect))
    (j/db-do-commands db-map (j/drop-table-ddl :vehicle))
    ))

(defn create-tables []
  (do
    (j/db-do-commands db-map
                      (j/create-table-ddl :report
                                          [[:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                                           [:report "text"]
                                           [:location "varchar(100)"]]))
    (j/db-do-commands db-map
                      (j/create-table-ddl :suspect
                                          [[:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                                           [:name "varchar(50)"]
                                           [:age "smallint(5)"]
                                           [:gender "char(1)"]
                                           [:height "smallint(5)"]
                                           [:weight "smallint(5)"]
                                           [:address "varchar(100)"]
                                           [:city "varchar(50)"]
                                           [:phone "varchar(50)"]
                                           ]))
    (j/db-do-commands db-map
                      (j/create-table-ddl :interview
                                          [[:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                                           [:interview "text"]
                                           [:name "varchar(50)"]]))
    (j/db-do-commands db-map
                      (j/create-table-ddl :vehicle
                                          [[:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
                                           [:colour "varchar(20)"]
                                           [:make "varchar(20)"]
                                           [:year "smallint(5)"]
                                           [:owner "varchar(50)"]
                                           [:license "varchar(20)"]
                                           ]))
    ))

(drop-tables)

(do
  (drop-tables)
  (create-tables))

(count (j/query db-map ["SELECT * FROM report"]))
(j/query db-map ["SELECT * FROM report WHERE location = 'Student Bar'"])
(j/query db-map ["SELECT * FROM interview"])
(j/query db-map ["SELECT * FROM vehicle"])
(j/query db-map ["SELECT * FROM suspect"])

(apply #(j/insert! db-map :report %) (:reports crimes/db))

(j/insert-multi! db-map :report (take 85 (:reports crimes/db)))


(defn populate-db []
  (do
    (j/insert-multi! db-map :suspect (:suspects crimes/db))
    (j/insert-multi! db-map :report (:reports crimes/db))
    (j/insert-multi! db-map :interview (:interviews crimes/db))
    (j/insert-multi! db-map :vehicle (:vehicles crimes/db))
    ))

(populate-db)

(:reports crimes/db)

(j/with-db-transaction [rep-conn db-map]
  (j/insert-multi! rep-conn (:reports crimes/db))
  (throw (Exception. "squl")))

(doseq [rows (partition 10 (:reports crimes/db))]
  (j/insert-multi! db-map :report rows))

(doseq [row (:reports crimes/db)]
  (j/insert! db-map :report row)
  (throw (Exception. "sql-exception")))
(j/with-db-transaction [sus-conn db-map]
  (doseq [row (:suspects crimes/db)]
    (j/insert! sus-conn :suspect row)))
(j/with-db-transaction [v-conn db-map]
  (doseq [row (:vehicles crimes/db)]
    (j/insert! v-conn :vehicle row)))
(j/with-db-transaction [int-conn db-map]
  (doseq [row (:interviews crimes/db)]
    (j/insert! int-conn :interview row)))


(defn populate-db [data]
  (do
      (doseq [row (:reports crimes/db)]
        (j/insert! rep-conn :report row))
      (doseq [row (:suspects crimes/db)]
        (j/insert! sus-conn :suspect row))
      (doseq [row (:vehicles crimes/db)]
        (j/insert! v-conn :vehicle row))
      (doseq [row (:interviews crimes/db)]
        (j/insert! int-conn :interview row))
    ))

(populate-db)

(drop-tables)
(create-tables)

(j/query db-map ["show tables"])

(j/db-do-commands db-map
                  (j/drop-table-ddl :report))

(j/db-do-commands db-map
                  (j/create-table-ddl :report
                                      [[:report "text"]
                                       [:location "varchar(20)"]]))

(j/db-do-commands db-map
                  (j/insert-multi!))

(doseq [row (:reports crimes/db)]
                    (j/insert! db-map :report row))

(j/db-do-commands db-map
                  (-> (insert-into :report)
                      (values (:reports crimes/db))
                      sql/format)
                  )


(doseq [row (:suspects crimes/db)]
  (j/insert! db-map :suspect row))

(j/db-do-commands db-map
                  (j/create-table-ddl :suspect
                                      [[:name "varchar(20)"]
                                       [:age "smallint(5)"]
                                       [:gender "char(1)"]
                                       [:height "smallint(5)"]
                                       [:weight "smallint(5)"]
                                       [:address "varchar(20)"]
                                       [:city "varchar(20)"]
                                       [:phone "varchar(20)"]
                                       ]))



(second (:interviews crimes/db))

(doseq [row (:interviews crimes/db)]
  (j/insert! db-map :interview row))

(j/db-do-commands db-map
                  (j/drop-table-ddl :interview))

(j/db-do-commands db-map
                  (j/create-table-ddl :interview
                                      [[:interview "text"]
                                       [:name "varchar(20)"]]))


(doseq [row (:vehicles crimes/db)]
  (j/insert! db-map :vehicle row))

(j/db-do-commands db-map
                  (j/create-table-ddl :vehicle
                                      [[:colour "varchar(20)"]
                                       [:make "varchar(20)"]
                                       [:year "smallint(5)"]
                                       [:license "varchar(20)"]
                                       ]))

(last
 (-> (insert-into :report)
     (values (:reports crimes/db))
     sql/format))

(j/query db-map ["SELECT * FROM report"])
(j/query db-map ["SELECT * FROM vehicle"])

