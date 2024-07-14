package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.*;
import io.github.winchest3r.backend.util.SimpleDatabase;

public class ExchangeDaoSimple implements ExchangeDao {
    SimpleDatabase db = SimpleDatabase.INSTANCE;

    @Override
    public List<ExchangeModel> getAll() {
        return db.exchangeRates;
    }

    @Override
    public ExchangeModel get(int baseId, int targetId) {
        Optional<ExchangeModel> exchangeOpt = db.exchangeRates
            .stream()
            .parallel()
            .filter(e -> 
                e.getBaseCurrency().getId() == baseId
                && e.getTargetCurrency().getId() == targetId)
            .findAny();
        return exchangeOpt.isPresent() ? exchangeOpt.get() : null;
    }

    @Override
    public ExchangeModel create(CurrencyModel base, CurrencyModel target, double rate) {
        ExchangeModel ex = new ExchangeModel(base, target, rate).setId(db.exchangeRates.size());
        db.exchangeRates.add(ex);
        return ex;
    }

    @Override
    public ExchangeModel update(int baseId, int targetId, double rate) {
        ExchangeModel sought = get(baseId, targetId);
        if (sought != null) {
            ExchangeModel updatedExchange = sought.setRate(rate);
            db.exchangeRates.set(sought.getId(), updatedExchange);
            sought = updatedExchange;
        }
        return sought;
    }
}
