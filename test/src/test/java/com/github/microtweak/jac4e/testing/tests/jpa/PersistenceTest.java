package com.github.microtweak.jac4e.testing.tests.jpa;

import com.github.microtweak.jac4e.testing.beans.Country;
import com.github.microtweak.jac4e.testing.beans.Customer;
import com.github.microtweak.jac4e.testing.beans.Gender;
import com.github.microtweak.jac4e.testing.beans.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest extends BaseJpaTest {

    @Test
    public void enumAsInnerClass() {
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setTotal(1000.0);

        save(order);
        clear();

        Order found = getEntityManager().find(Order.class, order.getId());

        assertAll(
            () -> assertNotNull(found),
            () -> assertEquals(order.getStatus(), found.getStatus())
        );
    }

    @Test
    public void enumAsNormalClass() {
        Customer customer = new Customer();
        customer.setName("Natasha Romanoff");
        customer.setGender(Gender.FEMALE);

        save(customer);
        clear();

        Customer found = getEntityManager().find(Customer.class, customer.getId());

        assertAll(
            () -> assertNotNull(found),
            () -> assertEquals(customer.getGender(), found.getGender())
        );
    }

    @Test
    public void enumWithManualApply() {
        Customer customer = new Customer();
        customer.setName("Tony Stark");
        customer.setCountry(Country.UNITED_STATES);

        save(customer);
        clear();

        Customer found = getEntityManager().find(Customer.class, customer.getId());

        assertAll(
            () -> assertNotNull(found),
            () -> assertEquals(customer.getCountry(), found.getCountry())
        );
    }

}
