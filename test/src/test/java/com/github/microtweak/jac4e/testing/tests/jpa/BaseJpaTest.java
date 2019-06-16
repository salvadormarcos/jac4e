package com.github.microtweak.jac4e.testing.tests.jpa;

import eu.drus.jpa.unit.api.Cleanup;
import eu.drus.jpa.unit.api.JpaUnit;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Cleanup
@ExtendWith(JpaUnit.class)
public class BaseJpaTest {

    @Getter(AccessLevel.PROTECTED)
    @PersistenceContext(unitName = "Jac4eTest")
    private EntityManager entityManager;

    protected void save(Object entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    protected void clear() {
        getEntityManager().clear();
    }

}
