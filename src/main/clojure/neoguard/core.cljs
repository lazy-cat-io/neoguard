(ns neoguard.core
  "Taken from https://github.com/borkdude/bebo"
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [goog.object :as gobj]
    [goog.string :as gstr]
    [goog.string.format]
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
                             'clojure.string {'capitalize str/capitalize
                                              'lower-case str/lower-case
                                              'spit       str/split
                                              'upper-case str/upper-case}
                             'clojure.set    {'difference   set/difference
                                              'intersection set/intersection
                                              'map-invert   set/map-invert
                                              'rename-keys  set/rename-keys
                                              'union        set/union}}
             :aliases       {'str 'clojure.string
                             'set 'clojure.set}
             :classes       {'js goog/global :allow :all}}))


(enable-console-print!)
(sci/alter-var-root sci/print-fn (constantly *print-fn*))


(defn sanitize
  [s]
  (some-> s (str/replace #"[“”]" "\"")))


(defn eval-string
  [text]
  (try
    (sci/eval-string text ctx)
    (catch :default _
      (-> text
          (sanitize)
          (sci/eval-string ctx)))))


(defn eval-file
  [url]
  (-> (if (str/starts-with? url "http")
        (-> (js/fetch url)
            (.then #(.text %)))
        (js/Deno.readTextFile url))
      (.then (fn [text]
               (scia/eval-string* ctx text)))))


(defn generate-captcha
  ([]
   (generate-captcha 20))
  ([n]
   (let [operators {"+" +, "-" -}
         operator  (rand-nth (keys operators))
         f         (get operators operator)
         args      [(rand-int n) (rand-int n)]]
     #js {:captcha (->> args
                        (str/join " ")
                        (gstr/format "(%s %s)" operator))
          :answer  (apply f args)})))
