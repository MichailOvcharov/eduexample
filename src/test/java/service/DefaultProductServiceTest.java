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
import ru.education.exceptions.EntityAlredyExistsException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.service.impl.DefaultProductService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
public class DefaultProductServiceTest {

    @Autowired
    private DefaultProductService defaultProductService;

    @Before
    public void createProductTest() {
        Product product = new Product(3,"product_test");
        defaultProductService.create(product);
    }
    // --- Find by all tests
    @Test
    public void findAllTest() {
        List<Product> products = defaultProductService.findAll();
        Assert.assertEquals(products.size(), 1);
    }
    // --- Find by id tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdNullProductIdException() {
        defaultProductService.findById(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void findByIdFormatProductException() {
        defaultProductService.findById("errorNumberFormat");
    }
    @Test(expected = EntityNotFoundException.class)
    public void findByIdNotFoundException() {
        defaultProductService.findById("4");
    }
    @Test
    public void findByIdTest() {
        Product product = defaultProductService.findById("3");
        Assert.assertNotNull(product);
        Assert.assertEquals(product.getName(),"product_test");
    }
    // --- Create tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductException() {
        defaultProductService.create(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void createNullProductIdException() {
        defaultProductService.create(new Product(null,"product_test2"));
    }
    @Test(expected = EntityAlredyExistsException.class)
    public void createAlredyExistsProductIException() {
        defaultProductService.create(new Product(3,"product_test"));
    }
    // --- Delete tests
    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteNullProductIdException() {
        defaultProductService.delete(null);
    }
    @Test(expected = EntityIllegalArgumentException.class)
    public void deleteFormatProductException() {
        defaultProductService.delete("errorNumberFormat");
    }
    @Test(expected = EntityNotFoundException.class)
    public void deleteNotFoundException() {
        defaultProductService.delete("4");
    }
    @After
    public void deleteProductTest() {
        Product product = defaultProductService.findById(3);
        Assert.assertNotNull(product);
        defaultProductService.delete(product.getId());
        List<Product> products = defaultProductService.findAll();
        Assert.assertNotNull(products);
        Assert.assertEquals(products.size(), 0);
    }
}
