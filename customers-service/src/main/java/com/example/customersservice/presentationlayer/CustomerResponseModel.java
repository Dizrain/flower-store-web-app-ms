package com.example.flowerstorewebapp.customermanagementsubdomain.presentationlayer;

import lombok.*;
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
