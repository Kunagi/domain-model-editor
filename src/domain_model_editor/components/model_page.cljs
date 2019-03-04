(ns domain-model-editor.components.model-page
  (:require
   ["@material-ui/core" :as mui]
   ["@material-ui/icons" :as icons]
   [re-frame.core :as rf]

   [facts-db.api :as db]
   [material-desktop.desktop.api :as desktop]
   [material-desktop.api :refer [<subscribe dispatch>]]
   [material-desktop.components :as mdc]
   [material-desktop.toolbar :as toolbar]
   [material-desktop.expansion-panel-list :as expansion-panel-list]))


(defn Entity
  [entity]
  [:div
   [:span
    {:style {:font-weight 600}}
    (-> entity :ident name)]])


(defn ElementsToolbar
  [element-type create-trigger-event]
  [toolbar/PaperToolbar
   {:title (str element-type)}
   [:> mui/IconButton
    {:style {:color :inherit}
     :on-click #(dispatch> create-trigger-event)}
    [:> icons/Add]]])


(defn EntityAndComponentsCards
  [entity all-entities-by-id]
  (let [component-entities (map (fn [component-entity-id]
                                  (get all-entities-by-id component-entity-id))
                                (-> entity :components))]
    [:div
     {:style {:margin-top (mdc/spacing 1)}}
     [:> mui/Card
      {:on-click #(dispatch> (-> entity :goto-event))
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
   [ElementsToolbar :entity (-> module :create-events :entity)]

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


(defn Module
  [module-subscription]
  (let [module (<subscribe module-subscription)]
    [:div
     [mdc/Double-H1 "Module" (:db/id module) "Module"]
     [mdc/Columns
      [EntitiesList module]]]))






(defn ModelWorkarea
  []
  (let [model (<subscribe [:domain-model-editor/model])]
    [:div
     ;; [:hr]
     ;; (mdc/Data (<subscribe :domain-model/modules-ids {:model-id model-id}))
     ;; [:hr]
     (into [:div]
           (mapv (fn [module-subscription]
                   [Module module-subscription])
                 (-> model :module-subscriptions)))]))
     ;; [:hr]
     ;; (mdc/Data (<subscribe [::db]))]))


;;; re-frame events



(rf/reg-sub
 ::db
 (fn [db _]
   db))
