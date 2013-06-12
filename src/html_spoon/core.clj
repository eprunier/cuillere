(ns html-spoon.core
  "This namespace is used to parse HTML from different kind of sources.

   For URL sources, you can supply requests options as a map:
     :user-agent -> set the user-agent header
     :timeout    -> set the connect + read timeout (default => 3000ms, 0 => disabled)
     :cookies    -> cookies as map (name can be a keyword) : {name value}

   Example :
   (post \"http://example.com/login\"
         {:username \"xxxxxx\"
          :password \"yyyyyy\"}
         {:user-agent \"Mozilla\"
          :timeout 0
          :cookies {:foo \"foo\"
                    :bar \"bar\"})
   "
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
               (= :user-agent k) (.userAgent conn v)
               (= :timeout k) (.timeout conn v)
               (= :cookies k) (.cookies conn (stringify-keys v))
               :else conn)))
          connection
          params))

(defn- ^Connection connect
  "Create a connection to url with optional parameters as map."
  [url & [params]]
  (-> (Jsoup/connect url)
      (add-connection-params params)))

(defn ^Document parse-url-get
  "Parse the HTML resulting of a HTTP GET."
  ([url]
     (parse-url-get url {}))
  ([url options]
     (-> url
         (connect options)
         .get)))

(defn ^Document parse-url-post
  "Parse the HTML resulting of a HTTP POST, with request parameters as a data map."
  ([url data]
     (parse-url-post url data {}))
  ([url data options]
     (-> url
         (connect options)
         (.data (stringify-keys data))
         .post)))
