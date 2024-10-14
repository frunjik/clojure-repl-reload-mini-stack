;; reloading pattern from
;; https://cognitect.com/blog/2013/06/04/clojure-workflow-reloaded

(ns user
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer :all]
            [clojure.test :as test]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [system :as system]
            [datomic.client.api :as d]
            [hiccup2.core :as h]
))

;; The single instance of the system
;; manipulated by the function below
(def system nil)

(defn init
  ;; Constructs the current development system
  []
  (alter-var-root #'system
                  (constantly (system/system))))

(defn setup []
  (load-file "setup.clj"))

(defn r []
  (load-file "repl.clj"))

(defn start
  ;; Starts the current development system
  []
  (alter-var-root #'system system/start))

(defn stop
  ;; Shuts down and destroys the current development system
  []
  (alter-var-root #'system
                  (fn [s] (when s (system/stop s)))))

(defn go
  ;; Initializes the current development system and starts it running
  ;; called from reset (below) after stop and refresh ...
  []
  (init)
  (start)
  (setup))

(defn reset []
  ;; The big reset - stop the system and start it again (after reloading source / resetting system namespace)
  (stop)
  (refresh :after 'user/go))

