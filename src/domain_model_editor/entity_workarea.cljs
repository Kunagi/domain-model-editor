(ns domain-model-editor.entity-workarea
  (:require
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as icons]

   [material-desktop.api :refer [<subscribe dispatch>]]
   [material-desktop.components :as mdc]
   [material-desktop.fieldset :as fieldset]
   [material-desktop.editing :as editing]))


(defn EntityCard [entity]
  [:> mui/Card
   [:> mui/CardContent
    [:h4 (-> entity :ident name)]
    [mdc/Data entity]
    [:hr]
    [fieldset/Fieldset
     :rows [
            {:fields [{:label "Identifier"
                       :value (-> entity :ident)
                       :on-click #(dispatch> [:desktop/form-dialog-triggered
                                              {:form-query [:domain-model-editor/element-fact-form
                                                            {:entity-id (:db/id entity)
                                                             :fact :ident}]}])}]}
            {:fields [{:label "Container"
                       :value (-> entity :container)}]}
            {:fields [{:label "Components"
                       :value (-> entity :components)}]}]]]])




(defn EntityWorkarea []
  (let [entity-page (<subscribe [:domain-model-editor/entity-page])]
    [:div
     [EntityCard (:entity entity-page)]]))
