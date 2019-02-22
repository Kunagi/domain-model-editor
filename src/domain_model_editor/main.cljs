(ns domain-model-editor.main
  (:require
   [re-frame.core :as rf]

   [material-desktop.app :as app]
   [material-desktop.ddapi-integration :as ddapi-integration]

   [material-desktop.components :as mdc]
   [material-desktop.desktop :as desktop]
   [facts-db.ddapi :as ddapi]

   [domain-model-editor.subs]
   [domain-model-editor.model-workarea :as model-workarea]))


(rf/reg-event-db
 ::init
 (fn [db _]
   (update-in db
              (ddapi-integration/api-db-path :domain-model :singleton)
              ddapi/events>
              [[:domain-model/module-created
                {:id :kunagi}]
               [:domain-model/entity-created
                {:module-id :kunagi
                 :container-id nil
                 :id :kunagi/product-backlog}]
               [:domain-model/entity-created
                {:module-id :kunagi
                 :container-id :kunagi/product-backlog
                 :id :kunagi/product-backlog-item}]])))


(defn Workarea []
  [model-workarea/ModelWorkarea :singleton])


(defn AppbarToolbar []
  [:div])


(defn Desktop []
  (desktop/Desktop
   :appbar {:title [mdc/Double-DIV "Domain Model Editor" "Demo"]
            :toolbar-components [[AppbarToolbar]]}
   :workarea {:components [[Workarea]]}))


(defn Root []
  [Desktop])

(defn ^:export start []
  (app/start [Root])
  (rf/dispatch [::init]))
