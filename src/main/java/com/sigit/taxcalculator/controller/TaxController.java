package com.sigit.taxcalculator.controller;

import com.sigit.taxcalculator.data.Bill;
import com.sigit.taxcalculator.data.BillResponse;
import com.sigit.taxcalculator.entity.Tax;
import com.sigit.taxcalculator.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.PostRemove;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sigit Kurniawan on 1/13/2019.
 * Main controller for Tax Calculator
 */

@RestController
@RequestMapping("/tax")
public class TaxController {

    @Autowired
    private TaxRepository taxRepository;

    /*
    Add tax objection to databases
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HashMap<String, Object>> add(@RequestBody Tax tax) {
        //status = 0 if failed, status = 1 if success
        String status = "0";
        String message = "Addition Failed";

        //check if request body has null or empty data
        if(!(tax.getName() == null || tax.getName().equals("") ||
                tax.getTaxCode() == 0 || tax.getPrice() == 0)){

            taxRepository.save(tax);
            status = "1";
            message = "Addition Succeeded";
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);

        if(status.equals("1")){
            return ResponseEntity.status(HttpStatus.CREATED).body(map);
        } else {
            return ResponseEntity.badRequest().body(map);
        }
    }

    @PostMapping("/delete")
    public void delete(@RequestBody String name){
        Tax tax = taxRepository.findByName(name);

        if(tax != null){
            taxRepository.delete(tax);
        }
    }

    /*
    Returns my bill response
     */
    @GetMapping("/getbill")
    public BillResponse getBill(){

        List<Tax> list = taxRepository.findAll();
        List<Bill> listOfResponse = new ArrayList<>();

        for (Tax tax : list) {
            listOfResponse.add(new Bill(tax));
        }

        return new BillResponse(listOfResponse);
    }

}
