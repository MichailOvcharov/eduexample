package service;

import config.TestConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.service.impl.DefaultProductService;
import ru.education.service.impl.SalesPeriodService;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
public class SalesPerodServiceTest {

    private Integer productId;
    private Long salesPeriodId;

    @Autowired
    private SalesPeriodService salesPeriodService;
    @Autowired
    private DefaultProductService defaultProductService;

    public SalesPerodServiceTest() {
        this.productId = new Integer(1);
        this.salesPeriodId = new Long(1);
    }

    @Before
    public void createProductTest() {
            Product product = new Product(productId,"product_test");
            defaultProductService.create(product);
            Date dateFrom = new GregorianCalendar(2020, 4 , 1).getTime();
            Date dateTo = new GregorianCalendar(2020, 4 , 5).getTime();
            SalesPeriod salesPeriod = new SalesPeriod(salesPeriodId, 200, dateFrom, dateTo , product);
            salesPeriodService.create(salesPeriod);
    }
    // --- Find all tests
    @Test
    public void findAllTest() {
        List<SalesPeriod> salesPeriods = salesPeriodService.findAll();
        Assert.assertEquals(salesPeriods.size(), 1);
    }
    // --- Find by id tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNullSalesPeriodIdException() {
        salesPeriodService.findById(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdFormatSalesPeriodException() {
        salesPeriodService.findById("errorNumberFormat");
    }
    @Test(expected = EntityNotFoundException.class)
    public void findByIdNotFoundException() {
        salesPeriodService.findById(salesPeriodId);
    }
    @Test
    public void findByIntegerIdTest() {
        List<SalesPeriod> salesPeriods = salesPeriodService.findAll();
        for(SalesPeriod salesPeriod: salesPeriods) {
            salesPeriodId = salesPeriod.getId();
        }
        SalesPeriod salesPeriod = salesPeriodService.findById(salesPeriodId.intValue());
        Assert.assertNotNull(salesPeriod);
        Assert.assertEquals(salesPeriod.getProduct().getName(),"product_test");
    }
    @Test
    public void findByLongIdTest() {
        List<SalesPeriod> salesPeriods = salesPeriodService.findAll();
        for(SalesPeriod salesPeriod: salesPeriods) {
            salesPeriodId = salesPeriod.getId();
        }
        SalesPeriod salesPeriod = salesPeriodService.findById(salesPeriodId);
        Assert.assertNotNull(salesPeriod);
        Assert.assertEquals(salesPeriod.getProduct().getName(),"product_test");
    }
    @Test
    public void findByStringIdTest() {
        List<SalesPeriod> salesPeriods = salesPeriodService.findAll();
        for(SalesPeriod salesPeriod: salesPeriods) {
            salesPeriodId = salesPeriod.getId();
        }
        SalesPeriod salesPeriod = salesPeriodService.findById(salesPeriodId.toString());
        Assert.assertNotNull(salesPeriod);
        Assert.assertEquals(salesPeriod.getProduct().getName(), "product_test");
    }
    // --- Create tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullSalesPeriodException() {
        salesPeriodService.create(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullSalesPeriodtIdException() {
        Product product = new Product(productId,"product_test");
        salesPeriodService.create(new SalesPeriod(
                null,
                3000,
                new GregorianCalendar(2020, 4 , 1).getTime(),
                new GregorianCalendar(2020, 4 , 5).getTime(),
                product)
                );
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductException() {
        salesPeriodService.create(
                new SalesPeriod(
                salesPeriodId,
                3000,
                new GregorianCalendar(2020, 4 , 1).getTime(),
                new GregorianCalendar(2020, 4 , 5).getTime(),
                null)
        );
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductIdException() {
        Product product = new Product(null, "product_test");
        salesPeriodService.create(
                new SalesPeriod(
                        salesPeriodId,
                        3000,
                        new GregorianCalendar(2020, 4 , 1).getTime(),
                        new GregorianCalendar(2020, 4 , 5).getTime(),
                        product)
        );
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullDateFromException() {
        Product product = new Product(productId + 1,"product_test");
        defaultProductService.create(product);
        salesPeriodService.create(new SalesPeriod(
                salesPeriodId,
                3000,
                null,
                new GregorianCalendar(2020, 4 , 5).getTime(),
                product)
        );
    }
    // --- Delete tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteNullSalesPeriodIdException() {
        salesPeriodService.delete(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteFormatSalesPeriodException() {
        salesPeriodService.delete("errorNumberFormat");
    }
    @Test(expected = EntityNotFoundException.class)
    public void deleteNotFoundException() {
        salesPeriodService.delete(salesPeriodId + 1);
    }
    @After
    public void deleteSalesPeriodTest() {
        List<SalesPeriod> salesPeriods = salesPeriodService.findAll();
        for(SalesPeriod salesPeriod: salesPeriods) {
            salesPeriodService.delete(salesPeriod.getId());
        }
        salesPeriods = salesPeriodService.findAll();
        Assert.assertNotNull(salesPeriods);
        Assert.assertEquals(salesPeriods.size(), 0);
        List<Product> products = defaultProductService.findAll();
        for(Product product: products) {
            defaultProductService.delete(product.getId());
        }
        products = defaultProductService.findAll();
        Assert.assertNotNull(products);
        Assert.assertEquals(products.size(), 0);
    }
}
