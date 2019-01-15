package com.sigit.taxcalculator;

import com.sigit.taxcalculator.entity.Tax;
import com.sigit.taxcalculator.repository.TaxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Sigit Kurniawan on 1/15/2019.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")

public class TaxRepositoryTests {

    private static final String name = "Lucky Stretch";
    private static final int taxCode = 1;
    private static final int price = 1000;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaxRepository repository;

    @Test
    public void findByNameShouldReturnTax() {
        this.entityManager.persist(new Tax(name, taxCode, price));
        this.entityManager.flush();

        Tax tax = this.repository.findByName(name);
        assertThat(tax.getName()).isEqualTo(name);
        assertThat(tax.getTaxCode()).isEqualTo(taxCode);
        assertThat(tax.getPrice()).isEqualTo(price);
    }

    @Test
    public void findByIdShouldReturnTax() {
        Tax temp = new Tax(name, taxCode, price);

        this.entityManager.persist(temp);
        this.entityManager.flush();

        Tax tax = this.repository.findById(temp.getId()).get();
        assertThat(tax).isNotNull();

        assertThat(tax.getName()).isEqualTo(name);
        assertThat(tax.getTaxCode()).isEqualTo(taxCode);
        assertThat(tax.getPrice()).isEqualTo(price);
    }

    @Test
    public void findByNameWhenNotFoundShouldReturnNull() {
        this.entityManager.persist(new Tax(name, taxCode, price));
        this.entityManager.flush();

        Tax tax = this.repository.findByName("Big Mac");
        assertThat(tax).isNull();
    }

    @Test
    public void findByIdWhenNotFoundShouldReturnNull() {
        Tax temp = new Tax(name, taxCode, price);

        this.entityManager.persist(temp);
        this.entityManager.flush();

        Optional<Tax> tax = this.repository.findById(temp.getId()+1L);
        assertThat(tax.isPresent()).isFalse();
    }
}
