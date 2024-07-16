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
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_EXCHANGE_RATES_BY_ID);

            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setInt(1, baseId);
                prepStat.setInt(2, targetId);
                try (ResultSet rs = prepStat.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("exchangeId");

                        String baseName = rs.getString("baseName");
                        String baseCode = rs.getString("baseCode");
                        String baseSign = rs.getString("baseSign");

                        String targetName = rs.getString("targetName");
                        String targetCode = rs.getString("targetCode");
                        String targetSign = rs.getString("targetSign");

                        double rate = rs.getDouble("rate");

                        CurrencyModel base = new CurrencyModel(
                            baseName, baseCode, baseSign).setId(baseId);
                        CurrencyModel target = new CurrencyModel(
                            targetName, targetCode, targetSign).setId(targetId);
                        
                        return new ExchangeModel(base, target, rate).setId(id);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + ": An exception during specific data receiving", e);
        }

        return null;
    }

    @Override
    public ExchangeModel get(String baseCode, String targetCode) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_EXCHANGE_RATES_BY_CODES);

            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                baseCode = baseCode.toUpperCase();
                targetCode = targetCode.toUpperCase();

                prepStat.setString(1, baseCode);
                prepStat.setString(2, targetCode);
                try (ResultSet rs = prepStat.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("exchangeId");

                        String baseName = rs.getString("baseName");
                        String baseSign = rs.getString("baseSign");
                        int baseId = rs.getInt("baseId");

                        String targetName = rs.getString("targetName");
                        String targetSign = rs.getString("targetSign");
                        int targetId = rs.getInt("targetId");

                        double rate = rs.getDouble("rate");

                        CurrencyModel base = new CurrencyModel(
                            baseName, baseCode, baseSign).setId(baseId);
                        CurrencyModel target = new CurrencyModel(
                            targetName, targetCode, targetSign).setId(targetId);
                        
                        return new ExchangeModel(base, target, rate).setId(id);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + ": An exception during specific data receiving", e);
        }

        return null;
    }

    @Override
    public ExchangeModel create(CurrencyModel base, CurrencyModel target, double rate) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.CREATE_NEW_EXCHANGE_RATE);

            int result = 0;
            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setInt(1, base.getId());
                prepStat.setInt(2, target.getId());
                prepStat.setDouble(3, rate);
                result = prepStat.executeUpdate();
            }

            if (result != 0) {
                return get(base.getId(), target.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + " An exception during object creation", e);
        }
        return null;
    }

    @Override
    public ExchangeModel update(CurrencyModel base, CurrencyModel target, double rate) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.UPDATE_EXCHANGE_RATE);

            int result = 0;
            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setDouble(1, rate);
                prepStat.setInt(2, base.getId());
                prepStat.setInt(3, target.getId());
                result = prepStat.executeUpdate();
            }

            if (result != 0) {
                return get(base.getId(), target.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + " An exception during object creation", e);
        }
        return null;
    }
}
