package io.github.winchest3r.backend.model;

public class Exchange {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;

    public Exchange(Currency base, Currency target, double rate) {
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

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }
}
