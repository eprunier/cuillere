(ns cuillere.document
  (:import [org.jsoup.nodes Document Element]
           [org.jsoup.select Elements]))

(defn ^Element head
  "Head of the HTML document."
  [^Document doc]
  (.head doc))

(defn title
  "Title of the HTML document."
  [^Document doc]
  (.title doc))

(defn ^Element body
  "Body of the HTML document."
  [^Document doc]
  (.body doc))

(defn ^Elements select
  [^Document doc selector]
  (.select doc selector))