package com.sigit.taxcalculator.repository;

import com.sigit.taxcalculator.entity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Sigit Kurniawan on 1/13/2019.
 */
public interface TaxRepository extends JpaRepository<Tax, Long> {
    Tax findByName(String name);
}
