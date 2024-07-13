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
    public CurrencyModel get(int id) {
        return data.get(id);
    }

    @Override
    public void create(String name, String code, String sign) {
        data.add(new CurrencyModel(name, code, sign).setId(data.size()));
    }
}
