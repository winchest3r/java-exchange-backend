package io.github.winchest3r.backend.dao;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import io.github.winchest3r.backend.model.CurrencyModel;
import io.github.winchest3r.backend.util.SqlQueries;
import io.github.winchest3r.backend.util.SqliteDataSource;

public class CurrencyDaoSqlite implements CurrencyDao {
    private DataSource dataSource = SqliteDataSource.getDataSource();

    @Override
    public List<CurrencyModel> getAll() {
        List<CurrencyModel> result = new ArrayList<CurrencyModel>();

        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_CURRENCIES);

            try (Statement stat = con.createStatement()) {
                ResultSet rs = stat.executeQuery(query);
                while (rs.next()) {
                    int id = rs.getInt("currencyId");
                    String name = rs.getString("fullName");
                    String code = rs.getString("code");
                    String sign = rs.getString("sign");

                    result.add(new CurrencyModel(name, code, sign).setId(id));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + ": An exception during data receiving", e);
        }

        return result;
    }

    @Override
    public CurrencyModel get(int id) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_CURRENCY_BY_ID);

            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setInt(1, id);
                try (ResultSet rs = prepStat.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("fullName");
                        String code = rs.getString("code");
                        String sign = rs.getString("sign");

                        return new CurrencyModel(name, code, sign);
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
    public CurrencyModel get(String code) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.GET_CURRENCY_BY_CODE);

            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setString(1, code);
                try (ResultSet rs = prepStat.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("currencyId");
                        String name = rs.getString("fullName");
                        String sign = rs.getString("sign");

                        return new CurrencyModel(name, code, sign).setId(id);
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
    public CurrencyModel create(String name, String code, String sign) {
        try (Connection con = dataSource.getConnection()) {
            String query = SqlQueries.readQuery(SqlQueries.CREATE_NEW_CURRENCY);

            int result = 0;
            try (PreparedStatement prepStat = con.prepareStatement(query)) {
                prepStat.setString(1, name);
                prepStat.setString(2, code);
                prepStat.setString(3, sign);
                result = prepStat.executeUpdate();
            }

            if (result != 0) {
                return get(code);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                getClass().getName() + " An exception during specific data receiving", e);
        }
        return null;
    }   
}
