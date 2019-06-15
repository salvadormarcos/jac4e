package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "orders")
public class Order {

    @AllArgsConstructor
    @EnumAttributeConverter
    public enum Status {

        PENDING('P'),

        SHIPPED('S'),

        COMPLETED('C');

        private char value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDate orderedAt = LocalDate.now();

    @Setter
    private Payment paymentMode;

    @Setter
    private Status status;

    @Setter
    private double total;

}
