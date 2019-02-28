(ns domain-model-editor.desktop
  (:require
   [re-frame.core :as rf]

   [material-desktop.api :refer [<subscribe]]
   [material-desktop.components :as mdc]
   [material-desktop.desktop :as desktop]
   [facts-db.ddapi :as ddapi]

   [domain-model-editor.ui-api]
   [domain-model-editor.subs]
   [domain-model-editor.model-workarea :as model-workarea]
   [domain-model-editor.entity-workarea :as entity-workarea]))


(def page->component
  {:model  model-workarea/ModelWorkarea
   :entity entity-workarea/EntityWorkarea})


(defn Workarea [current-page]
  (if-let [component-f (page->component current-page)]
    [(page->component current-page)]))


(defn AppbarToolbar []
  [:div])




(defn Desktop []
  (let [desktop (<subscribe [:domain-model-editor/desktop])]
    [mdc/Data (page->component (-> desktop :current-page))]
    (desktop/Desktop
     :appbar {:title [mdc/Double-DIV "Domain Model Editor" "Demo"]
              :toolbar-components [[AppbarToolbar]]}
     :workarea {:components [[Workarea (-> desktop :current-page)]]})))
