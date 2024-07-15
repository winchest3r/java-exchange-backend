CREATE TABLE IF NOT EXISTS currencies (
    currencyId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    code TEXT NOT NULL,
    fullName TEXT NOT NULL,
    sign TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS exchangeRates (
    exchangeId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    baseCurrencyId INTEGER NOT NULL,
    targetCurrencyId INTEGER NOT NULL,
    rate REAL NOT NULL,
    FOREIGN KEY (baseCurrencyId)
        REFERENCES currencies (currency_id)
        ON DELETE CASCADE,
    FOREIGN KEY (targetCurrencyId)
        REFERENCES currencies (currency_id)
        ON DELETE CASCADE
);