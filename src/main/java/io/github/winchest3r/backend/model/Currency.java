package io.github.winchest3r.backend.model;

public class Currency {
    private int id;
    private String name;
    private String code;
    private String sign;

    public Currency(String name, String code, String sign) {
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
