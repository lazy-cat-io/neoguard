(ns neoguard.core
  "Taken from https://github.com/borkdude/bebo"
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [goog.object :as gobj]
    [sci.async :as scia]
    [sci.core :as sci]
    [shadow.esm :as esm]))


(defn async-load-fn
  [{:keys [libname opts ctx ns]}]
  (let [[libname suffix] (str/split libname "$")]
    (-> (esm/dynamic-import libname)
        (.then
          (fn [js-lib]
            (let [js-lib (if suffix
                           (gobj/getValueByKeys js-lib (.split suffix "."))
                           js-lib)
                  munged (symbol (munge libname))]
              ;; register class globally in context
              (sci/add-class! ctx munged js-lib)
              (let [{:keys [as refer]} opts]
                (when as
                  ;; import class in current namespace with reference to globally
                  ;; registered class
                  (sci/add-import! ctx ns munged as))
                (when refer
                  (doseq [sym refer]
                    (let [prop        (gobj/get js-lib sym)
                          sub-libname (str munged "$" prop)]
                      ;; register sub-library globally
                      (sci/add-class! ctx sub-libname prop)
                      ;; add import to sub-library in current namespace
                      (sci/add-import! ctx ns sub-libname sym))))))
            {:handled true})))))


(def ctx
  (sci/init {:async-load-fn async-load-fn
             :namespaces    {'clojure.core   {'require scia/require}
                             'clojure.string {'upper-case str/upper-case
                                              'lower-case str/lower-case
                                              'capitalize str/capitalize
                                              'spit       str/split}
                             'clojure.set    {'intersection set/intersection
                                              'map-invert   set/map-invert
                                              'difference   set/difference
                                              'union        set/union
                                              'rename-keys  set/rename-keys}}
             :classes       {'js goog/global :allow :all}}))


(enable-console-print!)
(sci/alter-var-root sci/print-fn (constantly *print-fn*))


(defn eval-string
  [text]
  (scia/eval-string* ctx text))


(defn eval-file
  [url]
  (-> (if (str/starts-with? url "http")
        (-> (js/fetch url)
            (.then #(.text %)))
        (js/Deno.readTextFile url))
      (.then (fn [text]
               (eval-string text)))))
