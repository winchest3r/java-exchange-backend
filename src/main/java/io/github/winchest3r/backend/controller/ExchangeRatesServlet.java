package io.github.winchest3r.backend.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import io.github.winchest3r.backend.dao.CurrencyDaoSimple;
import io.github.winchest3r.backend.dao.ExchangeDaoSimple;
import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.model.ExchangeModel;
import io.github.winchest3r.backend.service.CurrencyService;
import io.github.winchest3r.backend.service.ExchangeService;

import io.github.winchest3r.backend.util.JsonUtils;

@WebServlet(name="ExchangeRatesServlet", urlPatterns = {"/api/exchange-rates/*"})
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeService exchangeService;
    CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        exchangeService = new ExchangeService(new ExchangeDaoSimple());
        currencyService = new CurrencyService(new CurrencyDaoSimple());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print("[");
                out.print(
                    exchangeService
                        .getAll()
                        .stream()
                        .map(JsonUtils::getExchange)
                        .collect(Collectors.joining(","))
                );
                out.print("]");
            } else {
                String[] splits = pathInfo.split("/");

                if (splits.length != 2) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError("Unaccepted path: " + pathInfo));
                    return;
                }

                if (!splits[1].matches("^[A-Za-z]{6}$")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError("Code doesn't match to CODE1CODE2 pattern: " + splits[1]));
                    return;
                }

                String code1 = splits[1].substring(0, 3);
                String code2 = splits[1].substring(3, splits[1].length());

                ExchangeModel exchange = exchangeService.getExchangeByCodePair(code1, code2);

                if (exchange == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError("Can't find code pair: " + code1 + ", " + code2));
                    return;
                }

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(JsonUtils.getExchange(exchange));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        Map<String, String[]> params = request.getParameterMap();
        try (PrintWriter out = response.getWriter()) {
            if (!params.containsKey("baseCurrencyCode")
                || !params.containsKey("targetCurrencyCode")
                || !params.containsKey("rate")) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError(
                    "Request doesn't have needed parameter(s): baseCurrencyCode, targetCurrencyCode, rate"
                ));
                return;
            }

            String baseCode = params.get("baseCurrencyCode")[0].toUpperCase();
            String targetCode = params.get("targetCurrencyCode")[0].toUpperCase();
            String rateString = params.get("rate")[0];

            if (!baseCode.matches("^[A-Za-z]{3}$") || !targetCode.matches("^[A-Za-z]{3}$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError(
                    "Base or/and target code don't match to three letter pattern: " + baseCode + ", " + targetCode
                ));
                return;
            }

            Double rate = null;
            try {
                rate = Double.valueOf(rateString);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Can't get rate value: " + e.getMessage()));
                return;
            }

            CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
            CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

            if (baseCurrency == null || targetCurrency == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(JsonUtils.getError("Can't find base or/and target currencies"));
                return;
            }

            if (exchangeService.getExchangeByCodePair(baseCode, targetCode) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print(JsonUtils.getError(
                    "Exchange rate '" + baseCode + " to " + targetCode + "' already exists"
                ));
                return;
            }

            ExchangeModel newExchange = exchangeService.createExchange(baseCurrency, targetCurrency, rate);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(JsonUtils.getExchange(newExchange));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        String rateString = request.getParameter("rate");
        String pathInfo = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {
            if (rateString == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Can't find 'rate' parameter"));
                return;
            }

            Double rate = null;
            try {
                rate = Double.valueOf(rateString);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Can't get rate value: " + e.getMessage()));
                return;
            }

            if (pathInfo == null || pathInfo.equals("/") || pathInfo.split("/").length != 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(JsonUtils.getError("Bad path. currency codes are needed"));
                return;
            }

            String codes = pathInfo.split("/")[1].toUpperCase();

            if (!codes.matches("^[A-Za-z]{6}$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Path doesn't match to CODE1CODE2 pattern: " + codes));
                return;
            }

            String baseCode = codes.substring(0, 3);
            String targetCode = codes.substring(3, codes.length());

            CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
            CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

            if (baseCurrency == null || targetCurrency == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(JsonUtils.getError(
                    "Can't find base or/and target currencies: " + baseCode + ", " + targetCode
                ));
                return;
            }

            ExchangeModel updatedExchange = exchangeService.updateExchange(baseCurrency, targetCurrency, rate);
            if (updatedExchange == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(JsonUtils.getError(
                    "Exchange rate '" + baseCode + " to " + targetCode + "' doesn't exist"
                ));
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(JsonUtils.getExchange(updatedExchange));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
