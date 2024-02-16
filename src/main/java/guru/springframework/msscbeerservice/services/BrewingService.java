package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.events.BrewBeerEvent;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final BeerMapper beerMapper;
    private final JmsTemplate jmsTemplate;


    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory () {
        beerRepository.findAll()
                .forEach(beer -> {
                    Integer invQQH = beerInventoryService.getOnHandInventory(beer.getId());

                    log.debug("Min OnHand is: {}", beer.getMinOnHand());
                    log.debug("Min OnHand is: {}", invQQH);

                    if(beer.getMinOnHand() >= invQQH) {
                        jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
                    }
                });
    }
}
