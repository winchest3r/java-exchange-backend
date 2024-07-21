package io.github.winchest3r.backend.model;

import java.math.*;

public class ExchangeModel {
    private int id;
    private CurrencyModel baseCurrency;
    private CurrencyModel targetCurrency;
    private BigDecimal rate;

    public ExchangeModel(CurrencyModel base, CurrencyModel target, BigDecimal rate) {
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    /**
     * Create new object with new id
     * 
     * @param id
     * @return new object
     */
    public ExchangeModel setId(int id) {
        ExchangeModel ret = new ExchangeModel(baseCurrency, targetCurrency, rate);
        ret.id = id;
        return ret;
    }

    public CurrencyModel getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyModel getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Create new object with new rate
     * 
     * @param rate
     * @return new object
     */
    public ExchangeModel setRate(BigDecimal rate) {
        ExchangeModel ret = new ExchangeModel(baseCurrency, targetCurrency, rate);
        ret.id = id;
        return ret;
    }
}
