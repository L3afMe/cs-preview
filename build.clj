(ns build
  (:refer-clojure :exclude [test])
  (:require [org.corfield.build :as bb]))

(def lib 'net.clojars.l3afme/cs-preview)
(def version "0.1.0-SNAPSHOT")
(def main 'l3afme.cs-preview)

(defn ci "Run the CI pipeline of tests (and build the uberjar)." [opts]
  (-> opts
      (assoc :lib lib :version version :main main)
      (bb/clean)
      (bb/uber)))
