package io.github.winchest3r.backend.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.stream.*;

import io.github.winchest3r.backend.dao.ExchangeDaoSimple;
import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.service.ExchangeService;

import io.github.winchest3r.backend.util.JsonUtils;

@WebServlet(name="ExchangeServlet", urlPatterns = {"/api/exchange-rates/*"})
public class ExchangeServlet extends HttpServlet {
    ExchangeService exchangeService;    

    @Override
    public void init(ServletConfig config) {
        exchangeService = new ExchangeService(new ExchangeDaoSimple());
        /*
         * TODO: Remove test data with sql data access object
         */
        CurrencyModel rub = new CurrencyModel("Russian Ruble", "RUB", "₽").setId(0);
        CurrencyModel usd = new CurrencyModel("US Dollar", "USD", "$").setId(1);
        CurrencyModel eur = new CurrencyModel("Euro", "EUR", "€").setId(2);
        CurrencyModel jpy = new CurrencyModel("Yen", "JPY", "¥").setId(3);
        CurrencyModel cny = new CurrencyModel("Yuan", "CNY", "¥").setId(4);

        exchangeService.createExchange(usd, eur, 0.9167);
        exchangeService.createExchange(eur, usd, 1.0908);
        exchangeService.createExchange(rub, eur, 0.0104);
        exchangeService.createExchange(eur, rub, 95.8475);
        exchangeService.createExchange(usd, rub, 87.8644);
        exchangeService.createExchange(rub, usd, 0.0114);
        exchangeService.createExchange(jpy, cny, 0.0460);
        exchangeService.createExchange(cny, jpy, 21.7568);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print("[");
                out.print(exchangeService.getAll().stream().map(JsonUtils::getExchange).collect(Collectors.joining(",")));
                out.print("]");
            } else {
                String[] splits = pathInfo.split("/");

                // if path is not like /api/currencies/CODE
                if (splits.length != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }

                /* 
                 * TODO: Find exchange rate by CODE PAIRS
                 * if there is no CODES like this - 404 (SC_NOT_FOUND)
                 * convert result to json
                 */
                
                out.print(String.format("{\"dummy\":%s}", splits[1]));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // TODO: doPost
}
