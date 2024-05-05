package com.example.paymentservice.datalayer;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //private identifier

    @Embedded
    private PaymentIdentifier paymentIdentifier; //public identifier

    @NotNull(message = "Amount cannot be null")
    private double amount;

    @NotNull(message = "Payment date cannot be null")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    public Payment(@NotNull double amount, @NotNull LocalDateTime paymentDate, @NotNull PaymentMethod paymentMethod) {
        this.paymentIdentifier = new PaymentIdentifier();
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }
}
