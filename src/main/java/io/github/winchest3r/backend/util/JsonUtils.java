package io.github.winchest3r.backend.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;

import io.github.winchest3r.backend.model.*;

/**
 * Class with static helpers to get data in JSON format.
 */
public class JsonUtils {

    /**
     * 
     * @return String with empty JSON "{}"
     */
    public static final String getEmpty() {
        return "{}";
    }

    public static String getCurrency(CurrencyModel cur) {
        return String.format(
            Locale.US,
            "{\"id\":%d,\"name\":\"%s\",\"code\":\"%s\",\"sign\":\"%s\"}",
            cur.getId(), cur.getName(), cur.getCode(), cur.getSign()
        );
    }

    public static String getExchange(ExchangeModel ex) {
        return String.format(
            Locale.US,
            "{\"id\":%d,\"baseCurrency\":%s,\"targetCurrency\":%s,\"rate\":%.4f}",
            ex.getId(), getCurrency(ex.getBaseCurrency()),
            getCurrency(ex.getTargetCurrency()), ex.getRate()
        );
    }

    public static String getConvertedAmount(
        CurrencyModel base,
        CurrencyModel target,
        BigDecimal rate,
        double amount) {
        
        return String.format(
            Locale.US,
            "{\"baseCurrency\":%s,\"targetCurrency\":%s,\"rate\":%s,\"amount\":%.4f,\"convertedAmount\":%s}",
            getCurrency(base), getCurrency(target), rate.toString(), amount, 
            rate.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.CEILING).toString()
        );
    }

    public static String getError(String message) {
        return String.format(Locale.US, "{\"error\":\"%s\"}", message);
    }
}
