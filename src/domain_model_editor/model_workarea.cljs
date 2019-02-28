(ns domain-model-editor.model-workarea
  (:require
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as icons]
   [material-desktop.api :refer [<subscribe dispatch>]]
   [material-desktop.components :as mdc]
   [material-desktop.toolbar :as toolbar]
   [material-desktop.editing :as editing]
   [material-desktop.expansion-panel-list :as expansion-panel-list]))


(defn Entity
  [entity]
  [:div
   [:span
    {:style {:font-weight 600}}
    (-> entity :ident name)]])


(defn ElementsToolbar
  [element-type module-id]
  [toolbar/PaperToolbar
   {:title (str element-type)}
   [:> mui/IconButton
    {:style {:color :inherit}
     :on-click #(dispatch> [:desktop/form-dialog-triggered
                            {:form-query [:domain-model-editor/new-element-form
                                          {:element-type element-type
                                           :module-id module-id}]}])}
     ;; :on-click #(dispatch> [:domain-model-editor/create-element-triggered
     ;;                        {:element-type element-type
     ;;                         :module-id module-id}])}
    [:> icons/Add]]])


(defn EntityAndComponentsCards
  [entity all-entities-by-id]
  (let [component-entities (map (fn [component-entity-id]
                                  (get all-entities-by-id component-entity-id))
                                (-> entity :components))]
    [:div
     {:style {:margin-top (mdc/spacing 1)}}
     [:> mui/Card
      {:on-click #(js/console.log "click!")
       :style {:cursor :pointer}}
               ;;:min-width "200px"}}
      [:> mui/CardContent
       [Entity entity]]]
     (into [:div
            {:style {:margin-left (mdc/spacing 2)}}]
           (mapv (fn [component-entity]
                   [EntityAndComponentsCards component-entity all-entities-by-id])
                 component-entities))]))


(defn EntitiesList
  [module]
  [:div
   ;; (mdc/Data module)
   [ElementsToolbar :entity (:db/id module)]

   (let [entities (-> module :entities)
         all-entities-by-id (reduce (fn [m entity]
                                      (assoc m (:db/id entity) entity))
                                    {}
                                    entities)
         root-entities (filter #(not (:container %))
                               entities)]
     (into [:div]
           (mapv (fn [root-entity]
                   [EntityAndComponentsCards root-entity all-entities-by-id])
                 root-entities)))])

     ;; [mdc/CardsColumn
     ;;  :cards (mapv (fn [entity]
     ;;                 [mdc/CardContent
     ;;                  [Entity entity]])
     ;;               root-entities)])])


(defn Module
  [module]
  [:div
   [mdc/Double-H1 "Module" (:db/id module)]
   [mdc/Columns
    [EntitiesList module]]])






(defn ModelWorkarea
  []
  (let [model (<subscribe [:domain-model-editor/model-details])]
    [:div
     ;; [:hr]
     ;; (mdc/Data (<subscribe :domain-model/modules-ids {:model-id model-id}))
     ;; [:hr]
     ;; (mdc/Data (<subscribe [:domain-model-editor/new-element-dialog]))
     ;; [:hr]
     (into [:div]
           (mapv (fn [module]
                   [Module module])
                 (-> model :modules)))]))
