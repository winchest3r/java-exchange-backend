package io.github.winchest3r.backend.service;

import java.util.*;

import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.Currency;

public class CurrencyService {
    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<Currency> getAllCurrencies() {
        return currencyDao.getAll();
    }

    public Currency getCurrencyByCode(String code) {
        return currencyDao.getByCode(code);
    }

    public void createCurrency(String name, String code, String sign) {
        currencyDao.create(new Currency(name, code, sign));
    }
}
