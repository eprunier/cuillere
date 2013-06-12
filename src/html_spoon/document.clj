(ns html-spoon.document
  (:import [org.jsoup.nodes Document Element]
           [org.jsoup.select Elements]))

(defn ^Element head
  "Return the head element of the HTML document."
  [^Document doc]
  (.head doc))

(defn title
  "Return the title of the HTML document."
  [^Document doc]
  (.title doc))

(defn ^Element body
  "Return the body of the HTML document."
  [^Document doc]
  (.body doc))

(defn ^Elements select
  "Return a selection of HTML elements."
  [^Document doc selector]
  (.select doc selector))
