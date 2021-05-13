package ru.education.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.annotation.AfterLoggable;
import ru.education.annotation.BeforeLoggable;
import ru.education.annotation.Loggable;
import ru.education.entity.Product;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.EntityAlredyExistsException;
import ru.education.exceptions.EntityHasDetailsException;
import ru.education.exceptions.EntityIllegalArgumentException;
import ru.education.exceptions.EntityNotFoundException;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.ProductService;

import java.util.List;

@Service
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;
    private final SalesPeriodRepository salesPeriodRepository;

    @Autowired
    public DefaultProductService(ProductRepository productRepository, SalesPeriodRepository salesPeriodRepository) {
        this.productRepository = productRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    @Loggable
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Loggable
    public Product findById(Object id) {
        Product product;
        if (id == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Integer parsedId;
        try {
            if (id instanceof Integer) {
                 parsedId = (Integer) id;
            }  else {
                 parsedId = Integer.valueOf((String) id);
            }
        } catch (NumberFormatException ex) {
            throw new EntityIllegalArgumentException(String.format("Не удалось перобразовать идентификатор "+
                    "к нужному типу, %s",ex)
                    );
        }
        product = productRepository.findOne(parsedId);
        if (product == null) {
            throw new EntityNotFoundException(Product.TYPE_NAME, parsedId);
        }
        return product;
    }

    @BeforeLoggable
    public Product create(Product product) {
        if (product == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (product.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Product existedProduct = productRepository.findOne(product.getId());
        if (existedProduct != null) {
            throw new EntityAlredyExistsException(Product.TYPE_NAME, product.getId());
        }
        return productRepository.save(product);
    }

    @Override
    @AfterLoggable
    public Product update(Product product) {
        if (product == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (product.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Product existedProduct = productRepository.findOne(product.getId());
        if (existedProduct == null) {
            throw new EntityNotFoundException(Product.TYPE_NAME, product.getId());
        }
        return productRepository.save(product);
    }

    public void delete(Object id) {
        Product product = this.findById(id);
        List<SalesPeriod> salesPeriod = salesPeriodRepository.findByProduct(product);
        if (salesPeriod.size() > 0) {
            throw new EntityHasDetailsException(SalesPeriod.TYPE_NAME, product.getId());
        }
        productRepository.delete(product);
    }
}
