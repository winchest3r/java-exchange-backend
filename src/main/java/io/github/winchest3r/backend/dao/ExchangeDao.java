package io.github.winchest3r.backend.dao;

import java.util.*;
import java.math.*;

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
    
    ExchangeModel get(String baseCode, String targetCode);

    ExchangeModel create(CurrencyModel base, CurrencyModel target, BigDecimal rate);

    ExchangeModel update(CurrencyModel base, CurrencyModel target, BigDecimal rate);
}
