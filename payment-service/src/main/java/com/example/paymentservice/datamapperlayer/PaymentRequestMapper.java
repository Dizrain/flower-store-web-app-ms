package com.example.paymentservice.datamapperlayer;


import com.example.paymentservice.datalayer.Payment;
import com.example.paymentservice.datalayer.PaymentIdentifier;
import com.example.paymentservice.presentationlayer.PaymentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Payment requestModelToEntity(PaymentRequestModel paymentRequestModel,
                                 PaymentIdentifier paymentIdentifier);
    }

