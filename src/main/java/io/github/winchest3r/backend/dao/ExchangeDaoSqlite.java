package io.github.winchest3r.backend.dao;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.model.ExchangeModel;
import io.github.winchest3r.backend.util.SqlQueries;
import io.github.winchest3r.backend.util.SqliteDataSource;

public class ExchangeDaoSqlite implements ExchangeDao {
    private DataSource dataSource = SqliteDataSource.getDataSource();

    @Override
    public List<ExchangeModel> getAll() {
        List<ExchangeModel> result = new ArrayList<>();

        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_EXCHANGE_RATES);

            try (Statement stat = con.createStatement()) {
                ResultSet rs = stat.executeQuery(query);
                while (rs.next()) {
                    int id = rs.getInt("exchangeId");

                    String baseName = rs.getString("baseName");
                    String baseCode = rs.getString("baseCode");
                    String baseSign = rs.getString("baseSign");
                    int baseId = rs.getInt("baseId");

                    String targetName = rs.getString("targetName");
                    String targetCode = rs.getString("targetCode");
                    String targetSign = rs.getString("targetSign");
                    int targetId = rs.getInt("targetId");

                    double rate = rs.getDouble("rate");

                    CurrencyModel base = new CurrencyModel(
                        baseName, baseCode, baseSign).setId(baseId);
                    CurrencyModel target = new CurrencyModel(
                        targetName, targetCode, targetSign).setId(targetId);
                    
                    result.add(new ExchangeModel(base, target, rate).setId(id));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + ": An exception during data receiving", e);
        }

        return result;
    }

    @Override
    public ExchangeModel get(int baseId, int targetId) {
        // TODO
        return null;
    }

    @Override
    public ExchangeModel get(String baseCode, String targetCode) {
        // TODO
        return null;
    }

    @Override
    public ExchangeModel create(CurrencyModel base, CurrencyModel target, double rate) {
        // TODO
        return null;
    }

    @Override
    public ExchangeModel update(int baseId, int targetId, double rate) {
        // TODO
        return null;
    }
}
