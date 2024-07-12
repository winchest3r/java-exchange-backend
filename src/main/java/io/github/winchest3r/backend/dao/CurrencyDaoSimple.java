package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.Currency;

/**
 * Currency data access object for tests.
 * Contains array to collect data.
 */
public class CurrencyDaoSimple implements CurrencyDao {
    private List<Currency> data = new ArrayList<>();

    @Override
    public List<Currency> getAll() {
        return data;
    }

    @Override
    public Currency getByCode(String code) {
        Optional<Currency> currency = data
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
    public void create(Currency currency) {
        currency.setId(data.size());
        data.add(currency);
    }
}
