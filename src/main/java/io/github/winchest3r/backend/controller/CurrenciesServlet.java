package io.github.winchest3r.backend.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import io.github.winchest3r.backend.service.*;
import io.github.winchest3r.backend.dao.*;
import io.github.winchest3r.backend.model.*;
import io.github.winchest3r.backend.util.*;

@WebServlet(name="CurrenciesServlet", urlPatterns = {"/api/currencies/*"})
public class CurrenciesServlet extends HttpServlet {
    CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        currencyService = new CurrencyService(new CurrencyDaoSimple());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            // if path is /api/currencies[/]
            if (pathInfo == null || pathInfo.equals("/")) {
                out.print("[");
                out.print(currencyService
                    .getAllCurrencies()
                    .stream()
                    .map(JsonUtils::getCurrency)
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

                if (!splits[1].matches("^[A-Za-z]{3}$")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(JsonUtils.getError("Code doesn't match to three letters format: " + splits[1]));
                    return;
                }

                CurrencyModel cur = currencyService.getCurrencyByCode(splits[1]);

                if (cur == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(JsonUtils.getError("Code not found: " + splits[1]));
                    return;
                }
                
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(JsonUtils.getCurrency(cur));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> params = request.getParameterMap();
        try (PrintWriter out = response.getWriter()) {
            if (!params.containsKey("name") || !params.containsKey("code") || !params.containsKey("sign")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Request doesn't have needed parameter(s): name, code, sign"));
                return;
            }

            String name = params.get("name")[0];
            String code = params.get("code")[0];
            String sign = params.get("sign")[0];

            if (!code.matches("^[A-Za-z]{3}$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(JsonUtils.getError("Code doesn't match to three letters format: " + code));
                return;
            }

            if (currencyService.getCurrencyByCode(code) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.print(JsonUtils.getError("Currency with code '" + code + "' already exists"));
                return;
            }

            CurrencyModel newCurrency = currencyService.createCurrency(name, code, sign);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(JsonUtils.getCurrency(newCurrency));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
