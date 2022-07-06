(ns neoguard.core-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [neoguard.core :as sut]))


(deftest eval-string
  (testing "quotes in telegram messages should be sanitized"
    (is (= "foo-42" (sut/eval-string "(str “foo-“ 42)")))
    (is (= "FOO" (sut/eval-string "(str/upper-case “foo”)")))
    (is (= "“BAR" (sut/eval-string "(str/upper-case \"“bar\")")))
    (is (= "BAZ“" (sut/eval-string "(str/upper-case \"baz“\")")))))



(deftest generate-captcha-test
  (testing "captcha must be generated correctly"
    (dotimes [_ 100]
      (let [{:keys [captcha answer]} (js->clj (sut/generate-captcha) :keywordize-keys true)]
        (is (string? captcha))
        (is (int? answer))
        (is (= answer (sut/eval-string captcha)))))))
