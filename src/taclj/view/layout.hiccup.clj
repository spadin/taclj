(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html" :charset "iso-8859-1"}]
  (if-let [meta-refresh (:meta-refresh *view-context*)]
    [:meta {:http-equiv "refresh" :content (str (:seconds meta-refresh) ";URL=" (:url meta-refresh))}])
  [:title "taclj"]
  (include-css "/stylesheets/taclj.css")
  (include-js "/javascript/taclj.js")]
 [:body
  [:h1 {:class "title"} "Taclj"
    [:em [:small "- tic tac toe"]]]

  (eval (:template-body joodo.views/*view-context*))
]]
