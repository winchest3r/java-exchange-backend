# Exchange Backend

| Request | Route                                                                | Example                                            |
|---------|----------------------------------------------------------------------|----------------------------------------------------|
| GET     | /api/currencies                                                      |                                                    |
| GET     | /api/currencies/CODE                                                 | /api/currencies/USD                                |
| GET     | /api/exchange-rates                                                  |                                                    |
| GET     | /api/exchange-rates/CODE1CODE2                                       | /api/exchange-rates/USDRUB                         |
| GET     | /api/exchange?**from**=BASE_CODE&**to**=TARGET_CODE&**amount**=VALUE | /api/exchange?from=EUR&to=JPY&amount=102.50        |
| POST    | /api/currencies?**name**=NAME&**code**=CODE&**sign**=SIGN            | /api/currencies?name=Russian+Ruble&code=RUB&sign=₽ |
| POST    | /api/exchange-rates?**baseCurrencyCode**=CODE&**targetCurrencyCode**=CODE&**rate**=VALUE | /api/exchange-rates/baseCurrencyCode=EUR&targetCurrencyCode=USD&rate=1.02 |
| PUT     | /exchange-rates/CODE1CODE2?**rate**=VALUE                            | /api/exchange-rates/EURUSD?rate=1.04               |