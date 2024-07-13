package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.CurrencyModel;

/**
 * Currency data access object for tests.
 * Contains array to collect data.
 */
public class CurrencyDaoSimple implements CurrencyDao {
    private List<CurrencyModel> data = new ArrayList<>();

    @Override
    public List<CurrencyModel> getAll() {
        return data;
    }

    @Override
    public CurrencyModel getByCode(String code) {
        Optional<CurrencyModel> currency = data
            .stream()
            .parallel()
            .filter(c -> c.getCode().equals(code))
            .findAny();
        if (currency.isPresent()) {
            return currency.get();
        }
        return null;
    }

    @Override
    public void create(CurrencyModel currency) {
        currency.setId(data.size());
        data.add(currency);
    }
}
