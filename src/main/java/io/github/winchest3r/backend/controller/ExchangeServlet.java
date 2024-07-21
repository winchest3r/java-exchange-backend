package io.github.winchest3r.backend.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.util.*;
import java.math.*;
import java.io.*;

import io.github.winchest3r.backend.service.*;
import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.*;

import io.github.winchest3r.backend.util.JsonUtils;

@WebServlet(name="ExchangeServlet", urlPatterns={"/api/exchange"})
public class ExchangeServlet  extends HttpServlet {
    CurrencyService currencyService;
    ExchangeService exchangeService;

    @Override
    public void init(ServletConfig config) {
        config.getServletContext().log(getClass().getName() + ": Initialization");

        exchangeService = new ExchangeService(new ExchangeDaoSqlite());
        currencyService = new CurrencyService(new CurrencyDaoSqlite());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        Map<String, String[]> params = request.getParameterMap();

        try (PrintWriter out = response.getWriter()) {
            try {
                if (!params.containsKey("from") || !params.containsKey("to") || !params.containsKey("amount")) {
                    String errorMessage = "Request doesn't have needed parameter(s): from, to, amount";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                String baseCode = params.get("from")[0].toUpperCase();
                String targetCode = params.get("to")[0].toUpperCase();
                String amountString = params.get("amount")[0];

                if (!baseCode.matches("^[A-Za-z]{3}$") || !targetCode.matches("^[A-Za-z]{3}$")) {
                    String errorMessage = "Base or/and target code don't match to three letter pattern: " + baseCode + ", " + targetCode;
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                Double amount = null;
                try {
                    amount = Double.valueOf(amountString);
                } catch (NumberFormatException e) {
                    String errorMessage = "Can't get amount value: " + e.getMessage();
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                if (baseCode.equals(targetCode)) {
                    CurrencyModel currency = currencyService.getCurrencyByCode(baseCode);
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(JsonUtils.getConvertedAmount(currency, currency, BigDecimal.ONE, amount));
                    return;
                }

                CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
                CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

                if (baseCurrency == null || targetCurrency == null) {
                    String errorMessage = "Can't find base or/and target currencies: " + baseCode + ", " + targetCode;
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                ExchangeModel exchange = exchangeService.getExchangeByCodePair(baseCode, targetCode);
                ExchangeModel reversedExchange = exchangeService.getExchangeByCodePair(targetCode, baseCode);
                
                BigDecimal rate = null;

                if (exchange != null) {
                    rate = exchange.getRate();
                } else if (reversedExchange != null) {
                    rate = BigDecimal.ONE.divide(reversedExchange.getRate(), 4, RoundingMode.HALF_UP);
                } else {
                    ExchangeModel crossToBase = null;
                    ExchangeModel crossToTarget = null;
                    
                    // Cross rate check
                    for (CurrencyModel cur : currencyService.getAllCurrencies()) {
                        crossToBase = exchangeService.getExchangeByCodePair(cur.getCode(), baseCode);
                        crossToTarget = exchangeService.getExchangeByCodePair(cur.getCode(), targetCode);

                        if (crossToBase != null && crossToTarget != null) {
                            break;
                        }
                    }

                    if (crossToBase == null || crossToTarget == null) {
                        String errorMessage = "Can't find base and target exchanges: " + baseCode + ", " + targetCode;
                        request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print(JsonUtils.getError(errorMessage));
                        return;
                    }

                    rate = crossToTarget.getRate().divide(crossToBase.getRate(), 4, RoundingMode.HALF_UP);
                }

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(JsonUtils.getConvertedAmount(baseCurrency, targetCurrency, rate, amount));
            } catch (Exception e) {
                String errorMessage = "Server exception occured: " + e.getMessage();
                request.getServletContext().log(errorMessage, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(JsonUtils.getError(errorMessage));
                return;
            }
        } catch (Exception e) {
            request.getServletContext().log(
                getClass().getName() + ": An exception occured during HTTP GET request", e);
        }
    }
}
