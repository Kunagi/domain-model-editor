(ns domain-model-editor.main
  (:require

   [domain-model-editor.ui-api]
   [domain-model-editor.subs]

   [domain-model-editor.desktop :as desktop]
   [material-desktop.app :as app]))


(defn Root []
  [desktop/Desktop])


(defn ^:export start []
  (app/start [Root]))
