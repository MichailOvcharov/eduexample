package ru.education.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.education.entity.SalesPeriod;
import ru.education.exceptions.*;
import ru.education.jpa.ProductRepository;
import ru.education.jpa.SalesPeriodRepository;
import ru.education.service.ProductService;

import java.util.List;

@Service
public class SalesPeriodService {


    private final ProductRepository productRepository;
    private final SalesPeriodRepository salesPeriodRepository;

    @Autowired
    public SalesPeriodService(ProductRepository productRepository, SalesPeriodRepository salesPeriodRepository) {
        this.productRepository = productRepository;
        this.salesPeriodRepository = salesPeriodRepository;
    }

    public List<SalesPeriod> findAll() {
        return salesPeriodRepository.findAll();
    }

    public SalesPeriod findById(Object id) {
        SalesPeriod salesPeriod;
        if (id == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        Long parsedId;
        try {
            if (id instanceof Long) {
                parsedId = (Long) id;
            }  else if (id instanceof Integer) {
                parsedId = new Long((Integer) id);
            } else {
                parsedId = Long.valueOf((String) id);
            }
        } catch (NumberFormatException ex) {
            throw new EntityIllegalArgumentException(String.format("Не удалось преобразовать идентификатор "+
                    "к нужному типу, %s",ex)
            );
        }
        salesPeriod = salesPeriodRepository.findOne(parsedId);
        if (salesPeriod == null) {
            throw new EntityNotFoundException(SalesPeriod.TYPE_NAME, parsedId);
        }
        return salesPeriod;
    }
    public SalesPeriod create(SalesPeriod salesPeriod) {
        if (salesPeriod == null) {
            throw new EntityIllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (salesPeriod.getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор объекта не может быть null");
        }
        if (salesPeriod.getProduct() == null) {
            throw new EntityIllegalArgumentException("Продукт не может быть null");
        }
        if (salesPeriod.getProduct().getId() == null) {
            throw new EntityIllegalArgumentException("Идентификатор продукта не может быть null");
        }
        if (salesPeriod.getDateFrom() == null) {
            throw new EntityIllegalArgumentException("Дата начала периода не может быть null");
        }
        SalesPeriod existedSalesPeriod = salesPeriodRepository.findOne(salesPeriod.getId());
        if (existedSalesPeriod != null) {
            throw new EntityAlredyExistsException(salesPeriod.TYPE_NAME, salesPeriod.getId());
        }
        List<SalesPeriod> lastSalesPeriod = salesPeriodRepository.findByDateToIsNullAndProductId(salesPeriod.getProduct().getId());
        if (lastSalesPeriod.size() > 0) {
            throw new EntityConflictException(String.format("В системе имеются открытые торговые периоды для продукта c id %s",salesPeriod.getProduct().getId()));
        }
        return salesPeriodRepository.save(salesPeriod);
    }
    public void delete(Object id) {
        SalesPeriod salesPeriod = this.findById(id);
        salesPeriodRepository.delete(salesPeriod);
    }
}
