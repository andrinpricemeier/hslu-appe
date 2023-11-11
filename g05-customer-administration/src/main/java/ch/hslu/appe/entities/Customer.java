/*
 * Copyright 2020 Roland Christen, HSLU Informatik, Switzerland
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

import java.util.Objects;

/**
 * Simple data model of a customer
 */
public final class Customer {

    private static final int NOID = -1;

    private int customerId;
    private String firstName;
    private String lastName;
    private String street;
    private String number;
    private String postalCode;
    private String city;



    /**
     * Default Constructor.
     */
    public Customer() {
        this(NOID, "unknown", "unknown", "unknown", "unknown", "unknown", "unknown");
    }

    /**
     * Constructor.
     *
     * @param customerId        unique id.
     * @param firstName         firstname.
     * @param lastName          lastname.
     * @param street            street.
     * @param number            number.
     * @param postalCode        postalcode.
     * @param city              city.

     */
    public Customer(final int customerId, final String firstName, final String lastName, final String street,
                    final String number, final String postalCode, final String city) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;

    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Customer with identical id are equal. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        return this.customerId == ((Customer) obj).customerId;
    }

    /**
     * Returns hash code based on the ID. {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.customerId);
    }

    /**
     * Returns a string representation of the customer. {@inheritDoc}.
     */
    @Override
    public String toString() {
        return "Customer[customerId=" + this.customerId + ", firstName='" + this.firstName + "', lastname='" + this.lastName
                + ", street=" + this.street + ", number=" + this.number + ", postalCode=" + this.postalCode
                + ", city=" + this.city + "]";
    }
}
