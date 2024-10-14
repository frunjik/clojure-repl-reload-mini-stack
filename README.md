
# Setup of REPL reload pattern for using pedestal / datomic

https://cognitect.com/blog/2013/06/04/clojure-workflow-reloaded

http://pedestal.io/
https://github.com/weavejester/hiccup

https://docs.datomic.com/datomic-overview.html


# use in repl live reloading of system

>(go)
>(reset)    # will run src/system.clj (and reset the system state - (pedestal server and datomic connection))
>(stop)
>(r)        # to only reload repl.clj
