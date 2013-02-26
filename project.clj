(defproject taclj "0.0.1"
  :description "A simple stand-alone webapp"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [joodo "1.1.2"]]

  :joodo-root-namespace taclj.root

  ; leiningen 2
  :profiles {:dev {:dependencies [[speclj "2.5.0"]]}}
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  :plugins [[speclj "2.5.0"]]

  ; leiningen 1
  :dev-dependencies [[speclj "2.5.0"]]
  :test-path "spec/"
  :java-source-path "src/"

  )
