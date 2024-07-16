package io.github.winchest3r.backend.util;

import java.io.*;
import jakarta.servlet.*;

public class SqlQueries {
    public static final String BASE_PATH = "WEB-INF/classes/sql/";

    public static final String CREATE_TABLES = BASE_PATH + "createTables.sql";

    public static final String FILL_TABLES = BASE_PATH + "fillTables.sql";

    public static final String GET_CURRENCIES = BASE_PATH + "getCurrencies.sql";

    public static final String GET_CURRENCY_BY_ID = BASE_PATH + "getCurrencyById.sql";

    public static final String GET_CURRENCY_BY_CODE = BASE_PATH + "getCurrencyByCode.sql";

    public static final String CREATE_NEW_CURRENCY = BASE_PATH + "createNewCurrency.sql";

    public static final String GET_EXCHANGE_RATES = BASE_PATH + "getExchangeRates.sql";

    public static final String GET_EXCHANGE_RATES_BY_ID = BASE_PATH + "getExchangeRatesById.sql";

    public static final String GET_EXCHANGE_RATES_BY_CODES = BASE_PATH + "getExchangeRatesByCodes.sql";

    public static final String CREATE_NEW_EXCHANGE_RATE = BASE_PATH + "createNewExchangeRate.sql";

    public static final String UPDATE_EXCHANGE_RATE = BASE_PATH + "updateExchangeRate.sql";

    private static ServletContext context;

    public static void setServletContext(ServletContext servletContext) {
        context = servletContext;
    }

    public static String readQuery(String path) {
        StringBuilder result = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                context.getResourceAsStream(path)))) {
            
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(
                SqlQueries.class.getName() + ": An exception occured during file reading", e);
        }
        return result.toString();
    }
}
