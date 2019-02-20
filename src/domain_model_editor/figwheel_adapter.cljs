(ns ^:figwheel-hooks domain-model-editor.figwheel-adapter
  (:require
   [reagent.core :as r]
   [material-desktop.init :as init]))


(defn Root []
  [:h1 "DME"])


(defn mount-app []
  (r/render
   [Root]
   (js/document.getElementById "app")))


(defn ^:export start
  []
  (init/install-roboto-css)
  ;;(rf/dispatch-sync [::init])
  (mount-app))


(defn ^:after-load on-figwheel-after-load []
  ;;(rf/dispatch-sync [::init])
  (mount-app))
