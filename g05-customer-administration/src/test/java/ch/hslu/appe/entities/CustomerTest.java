/*
 * Copyright 2020 Roland Gisler, Hochschule Luzern - Informatik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.appe.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Testcases for {@link Customer}.
 */
class CustomerTest {

    /*
     * Test method for
     * {@link Customer#Customer(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    final void testCustomerIntStringStringStringStringStringString() {
        final Customer customer = new Customer(1, "Firstname", "Lastname", "Street", "Number", "Postalcode", "City");
        assertAll("Customer", () -> assertThat(customer.getCustomerId()).isEqualTo(1),
                () -> assertThat(customer.getFirstName()).isEqualTo("Firstname"),
                () -> assertThat(customer.getLastName()).isEqualTo("Lastname"),
                () -> assertThat(customer.getStreet()).isEqualTo("Street"),
                () -> assertThat(customer.getNumber()).isEqualTo("Number"),
                () -> assertThat(customer.getPostalCode()).isEqualTo("Postalcode"),
                () -> assertThat(customer.getCity()).isEqualTo("City"));
    }

    /*
     * Test method for {@link Customer#Customer()}.
     */
    @Test
    final void testCustomerDefault() {
        final Customer customer = new Customer();
        assertAll("Customer", () -> assertThat(customer.getCustomerId()).isEqualTo(-1),
                () -> assertThat(customer.getFirstName()).isEqualTo("unknown"),
                () -> assertThat(customer.getLastName()).isEqualTo("unknown"),
                () -> assertThat(customer.getStreet()).isEqualTo("unknown"),
                () -> assertThat(customer.getNumber()).isEqualTo("unknown"),
                () -> assertThat(customer.getPostalCode()).isEqualTo("unknown"),
                () -> assertThat(customer.getCity()).isEqualTo("unknown"));
    }


    /*
     * Test method for {@link Customer#Customer()}.
     */
    @Test
    final void testCustomerDefaultSetter() {
        final Customer customer = new Customer();
        customer.setCustomerId(100);
        customer.setFirstName("Firstname");
        customer.setLastName("Lastname");
        customer.setStreet("Street");
        customer.setNumber("Number");
        customer.setPostalCode("Postalcode");
        customer.setCity("City");
        assertAll("Student", () -> assertThat(customer.getCustomerId()).isEqualTo(100),
                () -> assertThat(customer.getFirstName()).isEqualTo("Firstname"),
                () -> assertThat(customer.getLastName()).isEqualTo("Lastname"),
                () -> assertThat(customer.getStreet()).isEqualTo("Street"),
                () -> assertThat(customer.getNumber()).isEqualTo("Number"),
                () -> assertThat(customer.getPostalCode()).isEqualTo("Postalcode"),
                () -> assertThat(customer.getCity()).isEqualTo("City"));
    }

    /*
     * Test method for {@link Customer#equals(java.lang.Object)}.
     */
    @Test
    final void testEqualsObject() {
        EqualsVerifier.forClass(Customer.class).withOnlyTheseFields("customerId").suppress(Warning.NONFINAL_FIELDS).verify();
    }

    /*
     * Test method for {@link Customer#toString()}.
     */
    @Test
    final void testToString() {
        assertThat(new Customer(1, "Firstname", "Lastname", "Street", "Number", "Postalcode", "City")
                .toString())
                .contains("Firstname")
                .contains("Lastname")
                .contains("Street")
                .contains("Number")
                .contains("Postalcode")
                .contains("City");
    }

}
