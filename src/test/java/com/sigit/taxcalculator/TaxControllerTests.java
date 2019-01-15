package com.sigit.taxcalculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigit.taxcalculator.entity.Tax;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sigit Kurniawan on 1/15/2019.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TaxCalculatorApplication.class)
@AutoConfigureMockMvc
public class TaxControllerTests {

    private static final String name = "Lucky Stretch";
    private static final int taxCode = 2;
    private static final int price = 1000;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenTaxAddedSucceed() throws Exception {

        //taxCode 1 -> Food & Beverages
        Tax tax = new Tax(name, taxCode, price);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(tax);

        //add 1 tax
        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(requestJson)).andDo(print())
                .andExpect(jsonPath("$.status", containsString("1")))
                .andExpect(jsonPath("$.message", containsString("Addition Succeeded")))
                .andExpect(status().isCreated());

        //delete tax
        mockMvc.perform(post("/tax/delete").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(name)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenTaxAddedFailed() throws Exception {

        //if name is empty string, addition failed
        Tax tax = new Tax("", taxCode, price);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(tax);

        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(requestJson)).andDo(print())
                .andExpect(jsonPath("$.status", containsString("0")))
                .andExpect(jsonPath("$.message", containsString("Addition Failed")))
                .andExpect(status().is4xxClientError());

        //if taxCode is 0, addition failed
        tax = new Tax(name, 0, price);

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson=ow.writeValueAsString(tax);

        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(requestJson)).andDo(print())
                .andExpect(jsonPath("$.status", containsString("0")))
                .andExpect(jsonPath("$.message", containsString("Addition Failed")))
                .andExpect(status().is4xxClientError());

        //if price is 0, addition failed
        tax = new Tax(name, taxCode, 0);

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson=ow.writeValueAsString(tax);

        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(requestJson)).andDo(print())
                .andExpect(jsonPath("$.status", containsString("0")))
                .andExpect(jsonPath("$.message", containsString("Addition Failed")))
                .andExpect(status().is4xxClientError());

        //if tax not initialized, activation failed
        tax = new Tax();

        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = mapper.writer().withDefaultPrettyPrinter();
        requestJson=ow.writeValueAsString(tax);

        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
                .content(requestJson)).andDo(print())
                .andExpect(jsonPath("$.status", containsString("0")))
                .andExpect(jsonPath("$.message", containsString("Addition Failed")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenNoTaxGetBill() throws Exception {

        mockMvc.perform(get("/tax/getbill")).andDo(print())
                .andExpect(jsonPath("$.bill", hasSize(0)))
                .andExpect(jsonPath("$.priceSubTotal", is(0)))
                .andExpect(jsonPath("$.taxSubTotal", is(0.0)))
                .andExpect(jsonPath("$.grandTotal", is(0.0)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenTaxAddedAndThenGetBill() throws Exception {

        //taxCode 1 -> Food & Beverages
        Tax tax = new Tax(name, taxCode, price);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(tax);

        mockMvc.perform(post("/tax/add").contentType(APPLICATION_JSON_UTF8_VALUE )
        .content(requestJson)).andDo(print()).andExpect(status().isCreated());

        mockMvc.perform(get("/tax/getbill")).andDo(print())
                .andExpect(jsonPath("$.bill", hasSize(1)))
                .andExpect(jsonPath("$.bill[0].name", containsString(tax.getName())))
                .andExpect(jsonPath("$.bill[0].taxCode", is(tax.getTaxCode())))
                .andExpect(jsonPath("$.bill[0].type", containsString("Tobacco")))
                .andExpect(jsonPath("$.bill[0].refundable", containsString("No")))
                .andExpect(jsonPath("$.bill[0].price", is(1000))) //price
                .andExpect(jsonPath("$.bill[0].tax", is(30.0))) //10 + (2% of price)
                .andExpect(jsonPath("$.bill[0].amount", is(1030.0))) //price + tax
                .andExpect(jsonPath("$.priceSubTotal", is(1000))) //price
                .andExpect(jsonPath("$.taxSubTotal", is(30.0))) //10 + (2% of price)
                .andExpect(jsonPath("$.grandTotal", is(1030.0))) //price + tax
                .andExpect(status().isOk());
    }

}
