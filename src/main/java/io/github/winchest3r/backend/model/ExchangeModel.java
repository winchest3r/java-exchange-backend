package io.github.winchest3r.backend.model;

public class ExchangeModel {
    private int id;
    private CurrencyModel baseCurrency;
    private CurrencyModel targetCurrency;
    private double rate;

    public ExchangeModel(CurrencyModel base, CurrencyModel target, double rate) {
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyModel getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyModel getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
