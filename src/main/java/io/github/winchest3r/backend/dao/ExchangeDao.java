package io.github.winchest3r.backend.dao;

import java.util.*;

import io.github.winchest3r.backend.model.*;

public interface ExchangeDao {
    List<ExchangeModel> getAll();

    /**
     * Get rate between base and target currencies
     * 
     * @param baseCode three letter code of base currency
     * @param targetCode three letter code of target currency
     * @return Exchange object with rate data
     */
    ExchangeModel get(int baseId, int targetId);

    ExchangeModel create(CurrencyModel base, CurrencyModel target, double rate);

    ExchangeModel update(int baseId, int targetId, double rate);
}
