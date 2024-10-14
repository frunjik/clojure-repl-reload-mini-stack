;; reloading pattern from
;; https://cognitect.com/blog/2013/06/04/clojure-workflow-reloaded

(ns system
  (:require
   [webserver :as webserver]
   [database  :as database]))

(def system-config
  {;; :todo   "Your system properties here"
   :webserver   nil
   :database    nil
   :connection  nil})

(defn system
  ;; Returns a new (uninitialized) instance of the whole application
  []
  system-config)

(defn start
  ;; Performs side effects to initialize the system, acquire resources,
  ;; and start it running. Returns an updated instance of the system.
  [system]

  (let [
    database    (database/create)
    connection  (database/open)
    webserver   (webserver/start-dev connection)]

    (assoc system
          :database     database
          :connection   connection
          :webserver    webserver
          ))
  )

(defn stop
  ;; Performs side effects to shut down the system and release its
  ;; resources. Returns an updated instance of the system.
  [system]
  (assoc system
         :webserver   (webserver/stop-dev)
         :database    (database/destroy)
         :connection  nil
         ))
