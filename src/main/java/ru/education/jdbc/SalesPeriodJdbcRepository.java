package ru.education.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.education.entity.SalesPeriodJdbcDemo;
import ru.education.entity.Product;

import java.util.List;

@Repository
public class SalesPeriodJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SalesPeriodJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from public.sales_period", Integer.class);
    }

    public List<SalesPeriodJdbcDemo> getSalesPeriods() {
        return jdbcTemplate.query("select * from public.sales_period",
                (resultSet, i) ->
                new SalesPeriodJdbcDemo(
                        resultSet.getLong("id"),
                        resultSet.getInt("price"),
                        resultSet.getDate("date_from"),
                        resultSet.getDate("date_to"),
                        resultSet.getInt("product")
                        ));
    }
    public List<SalesPeriodJdbcDemo> getSalesPeriodsPriceIsHigher(long price) {
        return jdbcTemplate.query(String.format("select * from public.sales_period where price >= %s",price),
                (resultSet, i) ->
                        new SalesPeriodJdbcDemo(
                                resultSet.getLong("id"),
                                resultSet.getInt("price"),
                                resultSet.getDate("date_from"),
                                resultSet.getDate("date_to"),
                                resultSet.getInt("product")
                        ));
    }
    public List<Product> getProductsWithActivePeriod() {
        return jdbcTemplate.query("select p.id product_id, p.name product_name from public.product p inner join " +
                "public.sales_period sp on sp.product = p.id where sp.date_to is null",
                (resultSet, i) ->
                new Product (
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name")
                )
                );
    }
}
