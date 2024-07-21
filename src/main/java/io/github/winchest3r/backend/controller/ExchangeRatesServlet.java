package io.github.winchest3r.backend.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import io.github.winchest3r.backend.service.*;
import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.*;

import io.github.winchest3r.backend.util.JsonUtils;

@WebServlet(name="ExchangeRatesServlet", urlPatterns = {"/api/exchange-rates/*"})
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeService exchangeService;
    CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        config.getServletContext().log(getClass().getName() + ": Initialization"); 

        exchangeService = new ExchangeService(new ExchangeDaoSqlite());
        currencyService = new CurrencyService(new CurrencyDaoSqlite());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            try {
                if (pathInfo == null || pathInfo.equals("/")) {
                    StringBuilder data = new StringBuilder("[");
                    data.append(
                        exchangeService
                            .getAll()
                            .stream()
                            .map(JsonUtils::getExchange)
                            .collect(Collectors.joining(","))
                    );
                    data.append("]");
                    out.print(data.toString());
                } else {
                    String[] splits = pathInfo.split("/");

                    if (splits.length != 2) {
                        String errorMessage = "Unaccepted path: " + pathInfo;
                        request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(JsonUtils.getError(errorMessage));
                        return;
                    }

                    if (!splits[1].matches("^[A-Za-z]{6}$")) {
                        String errorMessage = "Code doesn't match to CODE1CODE2 pattern: " + splits[1];
                        request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(JsonUtils.getError(errorMessage));
                        return;
                    }

                    String code1 = splits[1].substring(0, 3);
                    String code2 = splits[1].substring(3, splits[1].length());

                    ExchangeModel exchange = exchangeService.getExchangeByCodePair(code1, code2);

                    if (exchange == null) {
                        String errorMessage = "Can't find code pair: " + code1 + ", " + code2;
                        request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print(JsonUtils.getError(errorMessage));
                        return;
                    }

                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(JsonUtils.getExchange(exchange));
                }
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        Map<String, String[]> params = request.getParameterMap();
        try (PrintWriter out = response.getWriter()) {
            try {
                if (!params.containsKey("baseCurrencyCode")
                    || !params.containsKey("targetCurrencyCode")
                    || !params.containsKey("rate")) {

                    String errorMessage = "Request doesn't have needed parameter(s): baseCurrencyCode, targetCurrencyCode, rate";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                String baseCode = params.get("baseCurrencyCode")[0].toUpperCase();
                String targetCode = params.get("targetCurrencyCode")[0].toUpperCase();
                String rateString = params.get("rate")[0];

                if (!baseCode.matches("^[A-Za-z]{3}$") || !targetCode.matches("^[A-Za-z]{3}$")) {
                    String errorMessage = "Base or/and target code don't match to three letter pattern: " + baseCode + ", " + targetCode;
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                BigDecimal rate = null;
                try {
                    rate = new BigDecimal(rateString);
                } catch (NumberFormatException e) {
                    String errorMessage = "Can't get rate value: " + e.getMessage();
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
                CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

                if (baseCurrency == null || targetCurrency == null) {
                    String errorMessage = "Can't find base or/and target currencies";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                if (exchangeService.getExchangeByCodePair(baseCode, targetCode) != null) {
                    String errorMessage = "Exchange rate '" + baseCode + " to " + targetCode + "' already exists";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                ExchangeModel newExchange = exchangeService.createExchange(baseCurrency, targetCurrency, rate);
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(JsonUtils.getExchange(newExchange));
            } catch (Exception e) {
                String errorMessage = "Server exception occured: " + e.getMessage();
                request.getServletContext().log(errorMessage, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(JsonUtils.getError(errorMessage));
                return;
            }
        } catch (Exception e) {
            request.getServletContext().log(
                getClass().getName() + ": An exception occured during HTTP POST request", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        String rateString = request.getParameter("rate");
        String pathInfo = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {
            try {
                if (rateString == null) {
                    String errorMessage = "Can't find 'rate' parameter";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                BigDecimal rate = null;
                try {
                    rate = new BigDecimal(rateString);
                } catch (NumberFormatException e) {
                    String errorMessage = "Can't get rate value: " + e.getMessage();
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                if (pathInfo == null || pathInfo.equals("/") || pathInfo.split("/").length != 2) {
                    String errorMessage = "Bad path. Currency codes are needed";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println(JsonUtils.getError(errorMessage));
                    return;
                }

                String codes = pathInfo.split("/")[1].toUpperCase();

                if (!codes.matches("^[A-Za-z]{6}$")) {
                    String errorMessage = "Path doesn't match to CODE1CODE2 pattern: " + codes;
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                String baseCode = codes.substring(0, 3);
                String targetCode = codes.substring(3, codes.length());

                CurrencyModel baseCurrency = currencyService.getCurrencyByCode(baseCode);
                CurrencyModel targetCurrency = currencyService.getCurrencyByCode(targetCode);

                if (baseCurrency == null || targetCurrency == null) {
                    String errorMessage = "Can't find base or/and target currencies: " + baseCode + ", " + targetCode;
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                ExchangeModel updatedExchange = exchangeService.updateExchange(baseCurrency, targetCurrency, rate);
                if (updatedExchange == null) {
                    String errorMessage = "Exchange rate '" + baseCode + " to " + targetCode + "' doesn't exist";
                    request.getServletContext().log(getClass().getName() + ": " + errorMessage);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError(errorMessage));
                    return;
                }

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(JsonUtils.getExchange(updatedExchange));
            } catch (Exception e) {
                String errorMessage = "Server exception occured: " + e.getMessage();
                request.getServletContext().log(errorMessage, e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(JsonUtils.getError(errorMessage));
                return;
            }
        } catch (Exception e) {
            request.getServletContext().log(
                getClass().getName() + ": An exception occured during HTTP PUT request", e);
        }

    }
}
