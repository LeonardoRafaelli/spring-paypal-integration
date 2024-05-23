package com.rafaelli.payment.config.paypal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PaypalController {
    private final PaypalService paypalService;
}
