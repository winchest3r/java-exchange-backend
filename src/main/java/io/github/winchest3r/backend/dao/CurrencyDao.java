package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.CurrencyModel;

public interface CurrencyDao {
    List<CurrencyModel> getAll();

    /**
     * 
     * @param code currency code (three letter thing)
     * @return currency if code found, null otherwise
     */
    CurrencyModel getByCode(String code);
    void create(CurrencyModel currency);
}
