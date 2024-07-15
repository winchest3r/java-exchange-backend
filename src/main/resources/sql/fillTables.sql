INSERT INTO currencies(currencyId, fullName, code, sign)
VALUES
    (0, "Russian Ruble", "RUB", "₽"),
    (1, "US Dollar", "USD", "$"),
    (2, "Euro", "EUR", "€"),
    (3, "Yen", "JPY", "¥"),
    (4, "Yuan", "CNY", "¥");

INSERT INTO exchangeRates(baseCurrencyId, targetCurrencyId, rate)
VALUES
    (1, 2, 0.9167),
    (2, 1, 1.0908),
    (0, 2, 0.0104),
    (2, 0, 95.8475),
    (1, 0, 87.8644),
    (0, 1, 0.0114),
    (3, 4, 0.0460),
    (4, 3, 21.7568),
    (1, 3, 157.9515);