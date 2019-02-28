(ns domain-model-editor.ui-api
  (:require
   [bindscript.api :refer [def-bindscript]]
   [facts-db.api :as db]
   [facts-db.ddapi :as ddapi :refer [def-event def-query def-api events> <query new-db]]))


;;; helpers

(defn domain-model-tree [db root-entity-id pattern]
  (-> db (db/fact :domain-model :db) (db/tree root-entity-id pattern)))

(defn domain-model-fact [db entity-id fact]
  (-> db (db/fact :domain-model :db) (db/fact entity-id fact)))

(defn domain-model-events> [db events]
  (db/++ db
         [{:db/id :domain-model
           :db (ddapi/events> (db/fact db :domain-model :db) events)}]))


(defn show-entity-page [db entity-id]
  (db/++ db
         [{:db/id :desktop
           :current-page :entity
           :current-entity entity-id}]))


;;; api

(def-api :domain-model-editor
  :autocreate-singleton-db? true
  :db-constructor
  (fn [db args]
    (-> db
        (db/++ [
                {:db/id :domain-model
                 :db (-> (ddapi/new-db :domain-model {})
                         (ddapi/events>
                          [[:domain-model/module-created
                            {:id :kunagi}]]))}

                {:db/id :desktop
                 :current-page :model}

                {:db/id :edit-element-fact-dialog
                 :open? false}

                {:db/id :new-element-dialog
                 :open? false}]))))



;;; edit-fact-form


(def-query :domain-model-editor/element-fact-form
  (fn [db {:keys [entity-id fact]}]
    (let [fact-value (domain-model-fact db entity-id fact)]
      {:title (str "Edit")
       :submit-text "Submit"
       :submit-event [:domain-model-editor/element-fact-form-submitted
                      {:entity-id entity-id
                       :fact fact}]
       :fields [{:type :text
                 :default-value fact-value
                 :required true ;; TODO required?
                 :id "fact"
                 :label (str fact)
                 :auto-focus true}]}))) ;; TODO move to desktop


(def-event :domain-model-editor/element-fact-form-submitted
  (fn [db {:keys [entity-id fact values]}]
    (-> db
        (domain-model-events>
         [[:domain-model/element-fact-updated
           {:entity-id entity-id
            :fact fact
            :value (get values "fact")}]]))))


;;; new-element-form

(def-query :domain-model-editor/new-element-form
  (fn [db {:keys [element-type module-id]}]
    {:title (str "New " element-type)
     :submit-text "Create"
     :submit-event [:domain-model-editor/new-element-form-submitted
                    {:element-type element-type
                     :module-id module-id}]
     :fields [{:type :text
               :default-value ""
               :required true
               :id "new-element-label"
               :label "Label"
               :auto-focus true}]})) ;; TODO move to desktop


(def-event :domain-model-editor/new-element-form-submitted
  (fn [db {:keys [values element-type module-id]}]
    (let [entity-id (db/new-uuid)
          ident (get values "new-element-label")
          ident (keyword (name module-id) ident)]
      (-> db

          (domain-model-events>
           [[:domain-model/entity-created ;; TODO element-created
             {:module-id module-id
              :id entity-id
              :ident ident
              :container-id nil}]])

          (db/++ [{:db/id :new-element-dialog
                   :open? false}])

          (show-entity-page entity-id)))))



;;; model


(def-query :domain-model-editor/model-details
  (fn [db _]
    (-> db
        (db/tree :domain-model {})
        :db
        (ddapi/<query [:domain-model/model-details]))))



;;; desktop


(def-query :domain-model-editor/entity-page
  (fn [db _]
    (let [entity-id (db/fact db :desktop :current-entity)]
      {:entity (domain-model-tree db entity-id {})})))


(def-query :domain-model-editor/desktop
  (fn [db _]
    (-> db (db/tree :desktop {}))))
