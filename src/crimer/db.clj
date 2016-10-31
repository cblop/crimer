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

(j/query db-map ["show tables"])

(j/db-do-commands db-map
                  (j/create-table-ddl :report
                                      [[:report "varchar(20)"]
                                       [:location "varchar(20)"]]))

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

(j/db-do-commands db-map
                  (j/create-table-ddl :interview
                                      [[:interview "varchar(20)"]
                                       [:name "varchar(20)"]]))


(j/db-do-commands db-map
                  (j/create-table-ddl :vehicle
                                      [[:colour "varchar(20)"]
                                       [:make "varchar(20)"]
                                       [:year "smallint(5)"]
                                       [:license "varchar(20)"]
                                       ]))

(-> (insert-into :report)
    (values (:reports crimes/db))
    sql/format)

(j/query db-map ["SELECT * FROM report"])

