(ns clj-ynab.core
  (:require
   [clj-ynab.api :as api]
   [clj-http.client :as http]
   [cheshire.core :as json]))

(defn init-client [config]
  (assoc config :client (api/client (:token config) http/request)))

(defn fetch [client request]
  (println "REQ" request)
  (api/->data (api/parse-response (client request))))

(defn budgets [{:keys [client]}]
  (:budgets (fetch client (api/budgets))))

(defn budget [{:keys [client budget-id]}]
  (:budget (fetch client (api/budget budget-id))))

(defn accounts [{:keys [client budget-id]}]
  (:accounts (fetch client (api/accounts budget-id))))

(defn account [{:keys [client budget-id]} account-id]
  (:account (fetch client (api/account budget-id account-id))))

(defn categories [{:keys [client budget-id]}]
  (:category_groups (fetch client (api/categories budget-id))))

(defn category [{:keys [client budget-id]} category-id]
  (:category (fetch client (api/category budget-id category-id))))

(defn transactions-for-account [{:keys [client budget-id]} account-id]
  (:transactions (fetch client (api/transactions-for-account budget-id account-id))))

(defn transactions-for-budget [{:keys [client budget-id]}]
  (:transactions (fetch client (api/transactions-for-budget budget-id))))

(defn transaction [{:keys [client budget-id]} tx-id]
  (:transaction (fetch client (api/transaction budget-id tx-id))))

(defn post-transaction [{:keys [client budget-id]} tx]
  (api/parse-response (client (api/post-transaction budget-id tx))))

(defn post-transactions [{:keys [client budget-id]} txs]
  (api/parse-response (client (api/post-transactions budget-id txs))))
