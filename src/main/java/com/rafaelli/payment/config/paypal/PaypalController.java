package com.rafaelli.payment.config.paypal;

import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

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
            String cancelUrl = "https://localhost:8080/payment/cancel";
            String successUrl = "https://localhost:8080/payment/success";
            
        } catch (PayPalRESTException e) {
            log.error("Error occurred:", e);
        }
    }

}
