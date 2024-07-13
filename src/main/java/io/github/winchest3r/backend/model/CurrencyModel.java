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

    /**
     * Returns new object with new id
     * 
     * @param id integer id
     * @return Currency object with new id
     */
    public CurrencyModel setId(int id) {
        CurrencyModel ret = new CurrencyModel(name, code, sign);
        ret.id = id;
        return ret;
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
