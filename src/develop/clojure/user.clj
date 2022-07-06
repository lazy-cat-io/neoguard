(ns user
  "Development helper functions."
  (:require
    [shadow.cljs.devtools.api :as shadow]))


(defn cljs-repl
  ([]
   (cljs-repl :neoguard))
  ([build-id]
   (shadow/node-repl {:build-id build-id})))
