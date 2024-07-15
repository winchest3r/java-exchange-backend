package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.CurrencyModel;

public interface CurrencyDao {
    List<CurrencyModel> getAll();

    CurrencyModel get(int id);
    CurrencyModel get(String code);
    CurrencyModel create(String name, String code, String sign);
}
