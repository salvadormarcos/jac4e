package com.github.microtweak.jac4e.testing.tests.jpa;

import com.github.microtweak.jac4e.testing.beans.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryTest extends BaseJpaTest {

    @Test
    public void compareAttributeInJpql() {
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setTotal(350.0);

        save(order);
        clear();

        Order found = getEntityManager()
                .createQuery("SELECT o FROM Order o WHERE o.status = :status", Order.class)
                .setParameter("status", Order.Status.PENDING)
                .getSingleResult();

        assertAll(
            () -> assertNotNull(found),
            () -> assertEquals(found.getId(), order.getId())
        );
    }

    @Test
    public void compareAttributeInNativeQuery() {
        Order order = new Order();
        order.setStatus(Order.Status.SHIPPED);
        order.setTotal(299.90);

        save(order);
        clear();

        Order found = (Order) getEntityManager()
                .createNativeQuery("SELECT * FROM orders o WHERE o.status = :status", Order.class)
                .setParameter("status", Order.Status.SHIPPED.getValue())
                .getSingleResult();

        assertAll(
            () -> assertNotNull(found),
            () -> assertEquals(found.getId(), order.getId()),
            () -> assertEquals(found.getStatus(), order.getStatus())
        );
    }



}
