package io.github.winchest3r.backend.service;

import java.util.*;

import io.github.winchest3r.backend.dao.ExchangeDao;
import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.model.ExchangeModel;

public class ExchangeService {
    ExchangeDao exchangeDao;

    public ExchangeService(ExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

    public List<ExchangeModel> getAll() {
        return exchangeDao.getAll();
    }

    public ExchangeModel getExchangeByCodePair(String baseCode, String targetCode) {
        Optional<ExchangeModel> exchangeOpt = exchangeDao
            .getAll()
            .stream()
            .parallel()
            .filter(e -> 
                e.getBaseCurrency().getCode().equals(baseCode.toUpperCase())
                && e.getTargetCurrency().getCode().equals(targetCode.toUpperCase()))
            .findAny();
        return exchangeOpt.isPresent() ? exchangeOpt.get() : null;
    }

    public ExchangeModel createExchange(CurrencyModel base, CurrencyModel target, double rate) {
        return exchangeDao.create(base, target, rate);
    }

    public ExchangeModel updateExchange(CurrencyModel base, CurrencyModel target, double rate) {
        return exchangeDao.update(base.getId(), target.getId(), rate);
    }
}
