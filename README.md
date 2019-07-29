# clj-ynab

Interact with [YNAB's API](https://api.youneedabudget.com/v1#/).


```clj
[clj-ynab "0.0.0"]
```

## Usage

``` clj
(def config {
    :token      "xx"
    :budget-id  "xx"
    :account-id "xx"})
    
;; get list of budgets
(ynab/budgets config)

;; post a transaction
(ynab/post-transaction 
  config
  { 
    :amount -60000,
    :date "2019-07-29",
    :account_id "XXXX",
    :approved false,
    :payee_name "Amazon",
    :cleared "cleared",
    :memo "things and stutff",
    })
    
;; See `clj-ynab.core` for more.

```

## License

Copyright © 2019 Casey Link

Distributed under the GNU Affero General Public License either version 3.0 or (at
your option) any later version.
