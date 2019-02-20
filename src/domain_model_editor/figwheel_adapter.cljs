(ns ^:figwheel-hooks domain-model-editor.figwheel-adapter
  (:require
   [material-desktop.figwheel-adapter :as figwheel-adapter]

   [domain-model-editor.main :as main]))


(defn ^:after-load on-figwheel-after-load []
  (figwheel-adapter/on-figwheel-after-load))


(main/start)
