(ns webserver
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.interceptor :as interceptor]
            [datomic.client.api :as d]
            [hiccup2.core :as h]))

;; for reloading webserver
(defonce server (atom nil))

(def html-response
  "If the response contains a key :html,
     it take the value of these key,
     turns into HTML via hiccup,
     assoc this HTML in the body
     and set the Content-Type of the response to text/html"
  {:name  ::html-response
   :leave (fn [{:keys [response]
                :as   ctx}]
            (if (contains? response :html)
              (let [html-body (->> response
                                   :html
                                   (h/html {:mode :html})
                                   (str "\n"))]
                (assoc ctx :response (-> response
                                         (assoc :body html-body)
                                         (assoc-in [:headers "Content-Type"] "text/html"))))
              ctx))})

(defn hello-conn
  [req]
  (let
   [conn (:datomic-conn req)]
    {:html  [:html
             [:head
              [:title "pedestal datomic"]]
             [:body
              [:p
               (str "Connection: " conn)]]]                                       ;; HERE - datomic connection
     :status 200}))

(defn datomic-conn-interceptor
  "Interceptor that associates a Datomic connection with the request."
  [datomic-conn]
  (interceptor/interceptor
   {:name ::datomic-conn
    :enter (fn [context]
             (assoc-in context [:request :datomic-conn] datomic-conn))}))

;; our route table
;; each route is an array with the fields
;; - the path
;; - the method
;; - the handler function
;; - the keyword :route-name (we will se more about this in a moment)
;; - a unique name for this route
(defn routes []
  #{["/"          :get `[html-response hello-conn]         :route-name :hello]
})

(defn start-dev [conn]
  (swap!  server (fn [state]
                   (some-> state http/stop)      ;; if there is (still) something running, stop it
                   (-> {::http/routes (route/routes-from (routes))
                        ::http/port   8080
                        ::http/join?  false
                        ::http/type   :jetty}
                       http/default-interceptors
                       (update ::http/interceptors into [(datomic-conn-interceptor conn)])  ;; Add the Datomic interceptor
                       http/dev-interceptors
                       http/create-server
                       http/start)))
  @server)

(defn stop-dev []
  (swap!  server http/stop)
  nil)
