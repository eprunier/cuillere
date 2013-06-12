(ns html-spoon.element
  (:import [org.jsoup.nodes Document Element]
           [org.jsoup.select Elements]))

(defn attr
  "Return the value of the attribute."
  [^Element element attr-name]
  (.attr element attr-name))

(defn text
  "Return the combined text of this element and all its children."
  [^Element element]
  (.text element))
