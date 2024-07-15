SELECT
    exchangeId,
    base.fullName AS baseName,
    base.code AS baseCode,
    base.sign AS baseSign,
    base.currencyId AS baseId,
    target.fullName AS targetName,
    target.code AS targetCode,
    target.sign AS targetSign,
    target.currencyId AS targetId,
    rate
FROM exchangeRates
    INNER JOIN currencies AS base ON exchangeRates.baseCurrencyId = base.currencyId
    INNER JOIN currencies AS target ON exchangeRates.targetCurrencyId = target.currencyId;