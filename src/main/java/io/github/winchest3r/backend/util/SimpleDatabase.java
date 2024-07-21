package io.github.winchest3r.backend.util;

import java.util.*;
import java.math.*;

import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.model.ExchangeModel;

public enum SimpleDatabase {
    INSTANCE;
    public List<CurrencyModel> currencies = new ArrayList<>();
    public List<ExchangeModel> exchangeRates = new ArrayList<>();

    private SimpleDatabase() {
        CurrencyModel rub = new CurrencyModel("Russian Ruble", "RUB", "₽").setId(0);
        currencies.add(rub);

        CurrencyModel usd = new CurrencyModel("US Dollar", "USD", "$").setId(1);
        currencies.add(usd);

        CurrencyModel eur = new CurrencyModel("Euro", "EUR", "€").setId(2);
        currencies.add(eur);

        CurrencyModel jpy = new CurrencyModel("Yen", "JPY", "¥").setId(3);
        currencies.add(jpy);

        CurrencyModel cny = new CurrencyModel("Yuan", "CNY", "¥").setId(4);
        currencies.add(cny);

        ExchangeModel usdEur = new ExchangeModel(usd, eur, BigDecimal.valueOf(0.9167)).setId(0);
        exchangeRates.add(usdEur);

        ExchangeModel eurUsd = new ExchangeModel(eur, usd, BigDecimal.valueOf(1.0908)).setId(1);
        exchangeRates.add(eurUsd);

        ExchangeModel rubEur = new ExchangeModel(rub, eur, BigDecimal.valueOf(0.0104)).setId(2);
        exchangeRates.add(rubEur);

        ExchangeModel eurRub = new ExchangeModel(eur, rub, BigDecimal.valueOf(95.8475)).setId(3);
        exchangeRates.add(eurRub);

        ExchangeModel usdRub = new ExchangeModel(usd, rub, BigDecimal.valueOf(87.8644)).setId(4);
        exchangeRates.add(usdRub);

        ExchangeModel rubUsd = new ExchangeModel(rub, usd, BigDecimal.valueOf(0.0114)).setId(5);
        exchangeRates.add(rubUsd);

        ExchangeModel jpyChy = new ExchangeModel(jpy, cny, BigDecimal.valueOf(0.0460)).setId(6);
        exchangeRates.add(jpyChy);

        ExchangeModel cnyJpy = new ExchangeModel(cny, jpy, BigDecimal.valueOf(21.7568)).setId(7);
        exchangeRates.add(cnyJpy);

        ExchangeModel usdJpy = new ExchangeModel(usd, jpy, BigDecimal.valueOf(157.9515)).setId(8);
        exchangeRates.add(usdJpy);

    }
}
