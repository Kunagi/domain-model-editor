(ns domain-model-editor.model-workarea
  (:require
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as icons]
   [material-desktop.api :refer [<subscribe]]
   [material-desktop.components :as mdc]
   [material-desktop.toolbar :as toolbar]
   [material-desktop.expansion-panel-list :as expansion-panel-list]))


(defn Entity
  [entity]
  [mdc/Data entity])




(defn ElementsList
  [elements-key ElementComponent model-id module]
  [:div

   [toolbar/PaperToolbar
    {:title (str elements-key)}
    [:> mui/IconButton
     {:style {:color :inherit}}
     [:> icons/Add]]]

   [mdc/Spacer 2]

   [mdc/CardsColumn
    :cards (mapv (fn [element]
                   [mdc/CardContent
                    [ElementComponent element]])
                 (get module elements-key))]])

(defn Module
  [model-id module-id]
  (let [module (<subscribe [:domain-model/module-details {:id :kunagi}])]
    [:div
     [mdc/Double-H1 "Module" module-id]
     [mdc/Columns
      [ElementsList :entities Entity model-id module]
      [ElementsList :entities Entity model-id module]]]))


(defn ModelWorkarea
  [model-id]
  [:div
   ;; (mdc/Data (<subscribe :domain-model/db))
   ;; [:hr]
   ;; (mdc/Data (<subscribe :domain-model/modules-ids {:model-id model-id}))
   (into [:div]
         (mapv (fn [module-id]
                 [Module model-id module-id])
               (<subscribe [:domain-model/modules-ids {:model-id model-id}])))])

