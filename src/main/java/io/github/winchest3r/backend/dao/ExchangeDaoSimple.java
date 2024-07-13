package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.*;

public class ExchangeDaoSimple implements ExchangeDao {
    private List<ExchangeModel> data = new ArrayList<>();

    @Override
    public List<ExchangeModel> getAll() {
        return data;
    }

    @Override
    public ExchangeModel get(int baseId, int targetId) {
        Optional<ExchangeModel> exchangeOpt = data
            .stream()
            .parallel()
            .filter(e -> 
                e.getBaseCurrency().getId() == baseId
                && e.getTargetCurrency().getId() == targetId)
            .findAny();
        return exchangeOpt.isPresent() ? exchangeOpt.get() : null;
    }

    @Override
    public void create(CurrencyModel base, CurrencyModel target, double rate) {
        data.add(new ExchangeModel(base, target, rate).setId(data.size()));
    }

    @Override
    public void update(int baseId, int targetId, double rate) {
        ExchangeModel sought = get(baseId, targetId);
        if (sought != null) {
            data.set(sought.getId(), sought.setRate(rate));
        }
    }
}
