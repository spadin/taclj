(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html" :charset "iso-8859-1"}]
  [:title "taclj"]
  (include-css "/stylesheets/taclj.css")
  (include-js "/javascript/taclj.js")]
 [:body
  (eval (:template-body joodo.views/*view-context*))
]]