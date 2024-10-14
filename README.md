
# Setup of REPL reload pattern for using pedestal / datomic

https://cognitect.com/blog/2013/06/04/clojure-workflow-reloaded

http://pedestal.io/
https://github.com/weavejester/hiccup

https://docs.datomic.com/datomic-overview.html


# Live reloading of system

>(go)


curl localhost:8080

to see injected connection


>(reset)    # will run src/system.clj (and reset the system state - (pedestal server and datomic connection))

curl localhost:8080

to see updated connection

>(stop)


## This will only load repl.clj


>(r)        # to only reload repl.clj
