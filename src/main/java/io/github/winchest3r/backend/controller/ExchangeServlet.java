package io.github.winchest3r.backend.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.util.*;
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
        currencyService = new CurrencyService(new CurrencyDaoSimple());
        exchangeService = new ExchangeService(new ExchangeDaoSimple());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        Map<String, String[]> params = request.getParameterMap();

        try (PrintWriter out = response.getWriter()) {
            if (!params.containsKey("from") || !params.containsKey("to") || !params.containsKey("amount")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Request doesn't have needed parameter(s): from, to, amount"));
                return;
            }

            String baseCode = params.get("from")[0].toUpperCase();
            String targetCode = params.get("to")[0].toUpperCase();
            String amountString = params.get("amount")[0];

            if (!baseCode.matches("^[A-Za-z]{3}$") || !targetCode.matches("^[A-Za-z]{3}$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError(
                    "Base or/and target code don't match to three letter pattern: " + baseCode + ", " + targetCode
                ));
                return;
            }

            Double amount = null;
            try {
                amount = Double.valueOf(amountString);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Can't get amount value: " + e.getMessage()));
                return;
            }

            if (baseCode.equals(targetCode)) {
                CurrencyModel currency = currencyService.getCurrencyByCode(baseCode);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(JsonUtils.getConvertedAmount(currency, currency, 1, amount));
                return;
            }

            CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
            CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

            if (baseCurrency == null || targetCurrency == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(JsonUtils.getError("Can't find base or/and target currencies: " + baseCode + ", " + targetCode));
                return;
            }

            ExchangeModel exchange = exchangeService.getExchangeByCodePair(baseCode, targetCode);
            ExchangeModel reversedExchange = exchangeService.getExchangeByCodePair(targetCode, baseCode);
            
            Double rate = null;

            if (exchange != null) {
                rate = exchange.getRate();
            } else if (reversedExchange != null) {
                rate = 1 / reversedExchange.getRate();
            } else {
                ExchangeModel usdToBase = exchangeService.getExchangeByCodePair("USD", baseCode);
                ExchangeModel usdToTarget = exchangeService.getExchangeByCodePair("USD", targetCode);

                if (usdToBase == null || usdToTarget == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError("Can't find base and target exchanges: " + baseCode + ", " + targetCode));
                    return;
                }

                rate = usdToTarget.getRate() / usdToBase.getRate();
            }

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(JsonUtils.getConvertedAmount(baseCurrency, targetCurrency, rate, amount));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
