CREATE TABLE IF NOT EXISTS currencies (
    currencyId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    code CHAR(3) NOT NULL,
    fullName VARCHAR(20) NOT NULL,
    sign VARCHAR(3) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchangeRates (
    exchangeId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    baseCurrencyId INTEGER NOT NULL,
    targetCurrencyId INTEGER NOT NULL,
    rate NUMERIC NOT NULL,
    FOREIGN KEY (baseCurrencyId)
        REFERENCES currencies (currency_id)
        ON DELETE CASCADE,
    FOREIGN KEY (targetCurrencyId)
        REFERENCES currencies (currency_id)
        ON DELETE CASCADE
);