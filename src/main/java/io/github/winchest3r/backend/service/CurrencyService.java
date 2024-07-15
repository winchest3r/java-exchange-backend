package io.github.winchest3r.backend.service;

import java.util.*;

import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.CurrencyModel;

public class CurrencyService {
    private CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyModel> getAllCurrencies() {
        return currencyDao.getAll();
    }

    public CurrencyModel getCurrencyByCode(String code) {
        return currencyDao.get(code);
    }

    public CurrencyModel createCurrency(String name, String code, String sign) {
        return currencyDao.create(name, code, sign);
    }
}
