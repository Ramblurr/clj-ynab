(ns clj-ynab.api
  (:require
   [clj-http.client :as http]
   [cheshire.core :as json]))

(def api-base "https://api.youneedabudget.com/v1")

(defn wrap-token [request token]
  (assoc-in request [:headers "Authorization"] (str "Bearer " token)))

(defn wrap-endpoint [request]
  (-> request
      (update :url #(apply format (str api-base %) (:url-params request)))
      (assoc :insecure? false)))

(defn wrap-json [request]
  (let [has-body?     (contains? request :body)
        body-request? (contains? #{:post :put} (:method request))]
    (println has-body? body-request?)
    (cond-> request
      true (assoc-in [:headers "Content-Type"] "application/json; charset=utf-8")
      (and body-request? has-body?) (update :body json/generate-string))))

(defn client
  [token http]
  (fn [request]
    (-> request
        (wrap-token token)
        wrap-endpoint
        wrap-json
        http)))

(defn parse-response [response]
  (cond-> response
    (string? (:body response))
    (update :body json/parse-string keyword)
    true
    (select-keys [:status :body :error])))

(defn response-error? [response]
  (contains? response :error))

(defn ->data [response]
  (get-in response [:body :data]))

(defn budgets []
  {:method :get
   :url    "/budgets"})

(defn budget [budget-id]
  {:method     :get
   :url        "/budgets/%s"
   :url-params [budget-id]})

(defn accounts [budget-id]
  {:method     :get
   :url        "/budgets/%s/accounts"
   :url-params [budget-id]})

(defn account [budget-id account-id]
  {:method     :get
   :url        "/budgets/%s/accounts/%s"
   :url-params [budget-id account-id]})

(defn categories [budget-id]
  {:method     :get
   :url        "/budgets/%s/categories"
   :url-params [budget-id]})

(defn category [budget-id cat-id]
  {:method     :get
   :url        "/budgets/%s/categories/%s"
   :url-params [budget-id cat-id]})

(defn transactions-for-account [budget-id account-id]
  {:method     :get
   :url        "/budgets/%s/accounts/%s/transactions"
   :url-params [budget-id account-id]})

(defn transactions-for-budget [budget-id]
  {:method     :get
   :url        "/budgets/%s/transactions"
   :url-params [budget-id]})

(defn transaction [budget-id tx-id]
  {:method     :get
   :url        "/budgets/%s/transactions/%s"
   :url-params [budget-id tx-id]})

(defn post-transaction [budget-id tx]
  {:method     :post
   :url        "/budgets/%s/transactions"
   :url-params [budget-id]
   :body       {:transaction tx}})

(defn post-transactions [budget-id txs]
  {:method     :post
   :url        "/budgets/%s/transactions"
   :url-params [budget-id]
   :body       {:transactions txs}})
