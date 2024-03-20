package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidator {
    private final BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrderDto) {
        Assert.notNull(beerOrderDto, "Beer Order should not be null");
        Assert.notNull(beerOrderDto.getBeerOrderLines(), "Beer Order Lines of Beer Order should not be null");
        Assert.notEmpty(beerOrderDto.getBeerOrderLines(), "Beer Order Lines of Beer Order should not be empty");
        AtomicBoolean valid = new AtomicBoolean(true);

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            Long count = beerRepository.countByUpc(beerOrderLineDto.getUpc());
            log.debug("Found {} order lines for UPC {}", count, beerOrderLineDto.getUpc());

            if(count <= 0) {
                valid.set(false);
            }
        });

        log.debug("The BeerOrder ID {} is valid {}", beerOrderDto.getId(), valid.get());
        return Boolean.valueOf(valid.get());
    }
}
