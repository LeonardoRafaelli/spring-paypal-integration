package com.rafaelli.payment.config.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;

    @GetMapping("/")
    public String home(){
        return "index";
    }


    @PostMapping("/payment/create")
    public RedirectView createPayment(){
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";

            Payment payment = paypalService.createPayment(
                    10.0,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment description",
                    cancelUrl,
                    successUrl
            );

            Optional<String> approvalUrl = getApprovalUrl(payment);
            if(approvalUrl.isPresent()) {
                return new RedirectView(approvalUrl.get());
            }

        } catch (PayPalRESTException e) {
            log.error("Error occurred:", e);
        }

        return new RedirectView("/payment/error");
    }

    private Optional<String> getApprovalUrl(Payment payment) {
        return payment.getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .map(Links::getHref)
                .findFirst();
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("token") String token
    ) {
        try {
            System.out.println("Got here");
            boolean paymentApproved = paypalService
                    .executePayment(paymentId, payerId)
                    .getState().equals("approved");

            if(paymentApproved) {
                return "paymentSuccess";
            };

        } catch (PayPalRESTException e) {
            log.error("Error ocurred:", e);
        }
        return "paymentSuccess";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }
    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }

}
