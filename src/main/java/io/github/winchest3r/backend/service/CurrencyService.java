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
        Optional<CurrencyModel> currencyOpt = currencyDao
            .getAll()
            .stream()
            .parallel()
            .filter(c -> c.getCode().equals(code.toUpperCase()))
            .findAny();
        return currencyOpt.isPresent() ? currencyOpt.get() : null;
    }

    public CurrencyModel createCurrency(String name, String code, String sign) {
        return currencyDao.create(name, code, sign);
    }
}
