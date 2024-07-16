UPDATE exchangeRates
SET rate = ?
WHERE baseCurrencyId = ? AND targetCurrencyId = ?;