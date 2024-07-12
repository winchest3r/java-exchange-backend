package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.Currency;

public interface CurrencyDao {
    List<Currency> getAll();

    /**
     * 
     * @param code
     * @return currency if code found, null otherwise
     */
    Currency getByCode(String code);
    void create(Currency currency);
}
