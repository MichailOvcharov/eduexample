package ru.education.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sales_period")
@Getter
@Setter
public class SalesPeriod {

    public static String TYPE_NAME = "Торговый период";

    @Id
    @Column(name = "id", nullable =  false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_periods_id_seq")
    @SequenceGenerator(name = "sales_periods_id_seq", sequenceName = "sales_periods_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "price")
    private long price;
    @Column(name = "date_from")
    private Date dateFrom;
    @Column(name = "date_to")
    private Date dateTo;
    @OneToOne
    @JoinColumn(name = "product", referencedColumnName = "id", nullable = false)
    private Product product;

    public SalesPeriod() {
    }

    public SalesPeriod(Long id, long price, Date dateFrom, Date dateTo, Product product) {
        this.id = id;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
