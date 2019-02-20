(ns domain-model-editor.main
  (:require
   [re-frame.core :as rf]

   [material-desktop.app :as app]
   [material-desktop.ddapi-integration :as ddapi-integration]

   [material-desktop.ddapi :as md-ddapi]
   [facts-db.ddapi :as ddapi]
   [domain-model.api :as dm]

   [domain-model-editor.subs]
   [domain-model-editor.editor :as editor]))


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





(defn Root []
  [editor/Editor])


(defn ^:export start []
  (app/start [Root])
  (rf/dispatch [::init]))
