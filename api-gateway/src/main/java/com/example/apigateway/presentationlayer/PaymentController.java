package com.example.apigateway.presentationlayer;


import com.example.apigateway.businesslayer.PaymentService;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentRequestModel;
import com.example.apigateway.presentationlayer.paymentdtos.PaymentResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {


    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity<List<PaymentResponseModel>> getPayments() {
        return ResponseEntity.ok().body(paymentService.getPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> getPaymentByPaymentId(@PathVariable String paymentId) {
        return ResponseEntity.ok().body(paymentService.getPaymentByPaymentId(paymentId));
    }


    @PostMapping()
    public ResponseEntity<PaymentResponseModel> addPayment(@RequestBody PaymentRequestModel paymentRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(paymentRequestModel));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> updatePayment(@RequestBody PaymentRequestModel paymentRequestModel, @PathVariable String paymentId) {
        return ResponseEntity.ok().body(paymentService.updatePayment(paymentRequestModel, paymentId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.removePayment(paymentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
