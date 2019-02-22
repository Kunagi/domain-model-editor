(ns domain-model-editor.ui-api
  (:require
   [bindscript.api :refer [def-bindscript]]
   [facts-db.api :as db]
   [facts-db.ddapi :as ddapi :refer [def-event def-query def-api events> <query new-db]]))


(def-api :domain-model-editor
  :autocreate-singleton-db? true
  :db-constructor
  (fn [db args]
    db))


(def-event :domain-model-editor/create-entity-triggered
  :handler
  (fn [db {:keys []}]
    (-> db
        (db/++ {:db/id :new-entity-dialog}))))
