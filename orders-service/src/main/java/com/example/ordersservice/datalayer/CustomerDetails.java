package com.example.ordersservice.datalayer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetails {
    private String customerId;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String contactNumber;

    @NotBlank
    private String address;
}
