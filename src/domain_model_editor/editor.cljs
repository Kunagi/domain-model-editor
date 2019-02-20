(ns domain-model-editor.editor
  (:require
   [material-desktop.api :refer [<subscribe]]
   [material-desktop.components :as mdc]
   [material-desktop.expansion-panel-list :as expansion-panel-list]))


(defn EntityDetails
  [entity]
  [mdc/Data entity])


(defn EntitiesList
  [model-id entities-ids]
  (let [entities (<subscribe :domain-model/entities {:model-id model-id
                                                     :ids entities-ids})]
    [:div
     ;; [mdc/Data entities-ids]
     ;; [:hr]
     ;; [mdc/Data entities]
     [expansion-panel-list/ExpansionPanelList
      :panels (mapv (fn [entity entities]
                      {:summary {:text (str (:db/id entity))}
                       :details {:component [EntityDetails entity]}})
                    entities)]]))


(defn Module
  [model-id module-id]
  (let [])
  [:div
   [:h1
    "module " module-id]
   [EntitiesList model-id [:kunagi/product-backlog :kunagi/product-backlog-item]]])


(defn Model
  [model-id]
  [:div
   ;; (mdc/Data (<subscribe :domain-model/db))
   ;; [:hr]
   ;; (mdc/Data (<subscribe :domain-model/modules-ids {:model-id model-id}))
   (into [:div]
         (mapv (fn [module-id]
                 [Module model-id module-id])
               (<subscribe :domain-model/modules-ids {:model-id model-id})))])


(defn Editor
  []
  [Model :singleton])
