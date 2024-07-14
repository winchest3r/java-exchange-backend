package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.util.SimpleDatabase;

/**
 * Currency data access object for tests.
 * Contains array to collect data.
 */
public class CurrencyDaoSimple implements CurrencyDao {
    SimpleDatabase db = SimpleDatabase.INSTANCE;

    @Override
    public List<CurrencyModel> getAll() {
        return db.currencies;
    }

    @Override
    public CurrencyModel get(int id) {
        return db.currencies.get(id);
    }

    @Override
    public CurrencyModel create(String name, String code, String sign) {
        CurrencyModel cur = new CurrencyModel(name, code, sign).setId(db.currencies.size());
        db.currencies.add(cur);
        return cur;
    }
}
