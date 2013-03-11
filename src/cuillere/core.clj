(ns cuillere.core
  "This namespace is used to parse HTML from different kind of sources.

   For URL sources, you can supply requests options as a map.
   Current available options are :
     :user-agent
     :timeout        ; default => 3000ms ; 0 => infinite
     :data

   Example :
   (post \"http://duckduckgo.com/html/\"
         {:user-agent \"Mozilla\"
          :timeout 0
          :data {:q \"clojure\"}})
   "
  (:refer-clojure :exclude [get])
  (:import [org.jsoup Jsoup Connection]
           [org.jsoup.nodes Document])
  (:use [clojure.walk :only [stringify-keys]]))

(declare ^{:private true :dynamic true} *connection*)

(defn ^Document parse-string
  "Parse HTML from string."
  [html]
  (-> html
      Jsoup/parse))

(defn ^Document parse-fragment
  "Parse a HTML fragment from string."
  [html]
  (-> html
      Jsoup/parseBodyFragment))

(defn ^Document parse-file
  "Load and parse a HTML file.
   The Optional base-uri parameter is used to resolve relative URLs."
  ([source charset]
     (parse-file source charset ""))
  ([source charset base-uri]
     (Jsoup/parse source charset base-uri)))

(defn- add-connection-params
  [^Connection connection params]
  (reduce (fn [conn param]
            (let [k (key param)
                  v (val param)]
              (cond
               (= :data k) (.data conn (stringify-keys v))
               (= :user-agent k) (.userAgent conn v)
               (= :timeout k) (.timeout conn v)
               :else conn)))
          connection
          params))

(defn- ^Connection connect
  "Create a connection to url with optional parameters."
  [url & [params]]
  (-> (Jsoup/connect url)
      (add-connection-params params)))

(defn ^Document get
  "Parse the HTML resulting of a HTTP GET."
  ([url]
     (get url {}))
  ([url options]
     (-> url
          (connect options)
          .get)))

(defn ^Document post
  "Parse the HTML resulting of a HTTP POST, with request params as a data map."
  ([url data]
     (post url data {}))
  ([url data options]
     (-> url
         (connect options)
         (.data (stringify-keys data))
         .post)))


