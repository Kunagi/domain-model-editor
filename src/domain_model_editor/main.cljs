(ns domain-model-editor.main
  (:require


   [material-desktop.app :as app]
   [domain-model.api]

   [domain-model-editor.mod]
   [domain-model-editor.components.desktop :refer [Desktop]]))


(defn Root []
  [Desktop])


(defn ^:export start []
  (app/start [Root]))
