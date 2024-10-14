(ns database
  (:require 
    [datomic.client.api :as d]
  ))

(def db-config {:server-type :datomic-local
                :system "dev"
                :storage-dir :mem})

(def db-client (d/client db-config))
(def db-name "dev")

(defn open []
  (let 
    [
      conn (d/connect db-client {:db-name db-name})
    ]
    conn
  )
)

(defn setup-db [conn]
  ;; do your initial db setup here ...
)

(defn create []
  (d/create-database db-client {:db-name db-name})
  (setup-db (open))
  db-client
)

(defn destroy []
  (d/delete-database db-client {:db-name db-name})
  nil)

(defn close []
  nil)
