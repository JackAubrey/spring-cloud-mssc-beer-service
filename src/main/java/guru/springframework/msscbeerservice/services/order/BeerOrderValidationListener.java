package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.events.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.events.ValidateBeerOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {
    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(@Payload ValidateBeerOrderRequest request) {
        log.debug("Received Validation BeerOrder Request {}", request);

        if(request == null) {
            throw new IllegalArgumentException("The Validate BeerOrder Request is null");
        } else {
            BeerOrderDto beerOrderDto = request.getBeerOrderDto();
            ValidateBeerOrderResult result = ValidateBeerOrderResult.builder()
                    .orderId(beerOrderDto.getId())
                    .isValid(beerOrderValidator.validateOrder(beerOrderDto))
                    .build();
            jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE, result);
            log.debug("Sent Validation BeerOrder Result {}", result);
        }
    }
}
