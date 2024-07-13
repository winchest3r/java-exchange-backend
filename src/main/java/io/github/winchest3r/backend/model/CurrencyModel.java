package io.github.winchest3r.backend.model;

public class CurrencyModel {
    private int id;
    private String name;
    private String code;
    private String sign;

    public CurrencyModel(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }
}
