package ru.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.education.entity.SalesPeriodJdbcDemo;
import ru.education.entity.SalesPeriod;
import ru.education.jdbc.SalesPeriodJdbcRepository;
import ru.education.entity.Product;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.model.Formatter;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TestControllers {

    private final Formatter formatter;

    private final ProductRepository productRepository;

    private final SalesPeriodJdbcRepository salesPeriodJdbcRepository;

    private final SalesPeriodRepository salesPeriodRepository;

    @Autowired
    public TestControllers(@Qualifier("fooFormatter") Formatter formatter, ProductRepository productRepositiry, SalesPeriodJdbcRepository salesPeriodJdbcRepository, SalesPeriodRepository salesPeriodRepository) {
        this.formatter = formatter;
        this.productRepository = productRepositiry;
        this.salesPeriodJdbcRepository = salesPeriodJdbcRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    @GetMapping("/hello")
    public String getHelo() {
        return "Hello, World!";
    }
    @GetMapping("/format")
    public String getFormat() {
        return formatter.format();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/sales/count")
    public Integer getSalsCount() {
        return salesPeriodJdbcRepository.count();
    }

    @GetMapping("/sales")
    public List<SalesPeriodJdbcDemo> getSalesPeriods() {
        return salesPeriodJdbcRepository.getSalesPeriods();
    }

    @GetMapping("/sales/byhigherprice")
    public List<SalesPeriodJdbcDemo> getSalesPeriodsbyHigherPrice() {
        return salesPeriodJdbcRepository.getSalesPeriodsPriceIsHigher(90);
    }

    @GetMapping("/products/sales/active")
    public List<Product> getproductsWithActivePeriod() {
        return salesPeriodJdbcRepository.getProductsWithActivePeriod();
    }
    @GetMapping("/sales/jpa")
    public List<SalesPeriod> getSalesPeriodsJpa() { return salesPeriodRepository.findAll();}

    @PostMapping("/sales/jpa")
    public SalesPeriod addSalesPeriodsJpa(@RequestBody SalesPeriod salesPeriod) {
        return salesPeriodRepository.save(salesPeriod);
    }
    @GetMapping("/sales/jpa/max/price")
    public Integer getMaxPriceByProductId() {return salesPeriodRepository.getMaxPriceByProductId(1);}

    @GetMapping("/sales/jpa/exists/price")
    public Boolean existsByPrice() {return salesPeriodRepository.existsByPrice(50);}

    @GetMapping("/sales/jpa/active")
    public List<SalesPeriod> findByDateToIsNull() {return salesPeriodRepository.findByDateToIsNull();}

    @GetMapping("/sales/jpa/byproductname")
    public List<SalesPeriod> findByProductName() {return  salesPeriodRepository.findByProductName("bike");}
}
