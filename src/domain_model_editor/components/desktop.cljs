(ns domain-model-editor.components.desktop
  (:require
   [re-frame.core :as rf]

   [facts-db.ddapi :as ddapi]
   [material-desktop.api :refer [<subscribe]]
   [material-desktop.components :as mdc]
   [material-desktop.desktop.components.desktop :as desktop]

   [domain-model-editor.components.model-page :refer [ModelWorkarea]]
   [domain-model-editor.components.entity-page :refer [EntityWorkarea]]))


(defn create-page [title workarea-component]
  {:appbar {:title [mdc/Double-DIV title "Domain Model Editor"]}
   :workarea {:components [[workarea-component]]}})


(def pages
  {:domain-model-editor/model  (create-page "Domain Model" ModelWorkarea)
   :domain-model-editor/entity (create-page "Entity" EntityWorkarea)})


(defn Desktop []
  (desktop/PagedDesktop
   {:pages pages
    :home-page :domain-model-editor/model}))
