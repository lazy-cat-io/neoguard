(ns neoguard.core-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [neoguard.core :as sut]))


(deftest generate-captcha-test
  (testing "captcha must be generated correctly"
    (dotimes [_ 100]
      (let [{:keys [captcha answer]} (js->clj (sut/generate-captcha) :keywordize-keys true)]
        (is (string? captcha))
        (is (int? answer))
        (is (= answer (sut/eval-string captcha)))))))

