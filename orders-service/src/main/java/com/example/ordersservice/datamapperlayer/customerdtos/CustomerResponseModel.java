package com.example.ordersservice.datamapperlayer.customerdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseModel extends RepresentationModel<CustomerResponseModel> {

    String customerId;
    String name;
    String email;
    String contactNumber;
    String address;
}
