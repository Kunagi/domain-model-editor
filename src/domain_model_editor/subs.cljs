(ns domain-model-editor.subs
  (:require
   [re-frame.core :as rf]
   [facts-db.api :as db]
   [facts-db.ddapi :as ddapi]
   [domain-model.api :as dm]))


(rf/reg-sub
 :domain-model-editor/model
 (fn []
   (rf/subscribe [:domain-model/db]))
 (fn [domain-model-db _]
   (if-not domain-model-db
     nil
     (let [model (get domain-model-db :model)]
       (-> model

           ;; provide subscriptions for modules
           (assoc :module-subscriptions
                  (map (fn [module-id] [:domain-model-editor/module {:module-id module-id}])
                       (get model :modules))))))))



(defn assoc-goto-event-on-entity [entity]
  (assoc entity :goto-event [:material-desktop/activate-page
                             {:page-key :domain-model-editor/entity
                              :page-args {:entity-id (:db/id entity)}}]))


(rf/reg-sub
 :domain-model-editor/module
 (fn []
   (rf/subscribe [:domain-model/db]))
 (fn [domain-model-db [_ {:keys [module-id]}]]
   (if-not domain-model-db
     nil
     (-> domain-model-db
         (db/tree module-id {:entities {}})

         (update :entities #(mapv assoc-goto-event-on-entity %))

         ;; provide events for creating elements
         (assoc :create-events {:entity [:domain-model-editor/create-entity-triggered
                                         {:module-id module-id}]})))))


(rf/reg-sub
 :domain-model-editor/entity
 (fn []
   [(rf/subscribe [:domain-model/db])
    (rf/subscribe [:material-desktop/current-page-args])])
 (fn [[domain-model-db current-page-args] _]
   (if-not domain-model-db
     nil
     (let [entity-id (:entity-id current-page-args)]
       (-> domain-model-db
           (db/tree entity-id {:module {}}))))))
