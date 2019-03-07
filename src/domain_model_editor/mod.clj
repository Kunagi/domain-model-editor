(ns domain-model-editor.mod
 (:require
  [compojure.core :as compojure]

  [appkernel.api :as app]
  [facts-db.ddapi :as ddapi]
  [domain-model.module.api :as dmm-api]))

(tap> ::loading)


(defn server-domain-model
  [req]
  (let [module :kunagi]
    (str
     (ddapi/new-db :domain-model-module {:ident module}))))


(app/def-query-responder ::domain-model
  :query :http-server/routes
  :f (fn [db args]
       [(compojure/GET "/domain-model" [] server-domain-model)]))
