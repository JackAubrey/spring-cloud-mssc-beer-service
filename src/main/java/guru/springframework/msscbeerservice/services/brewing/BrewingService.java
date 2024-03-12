package guru.springframework.msscbeerservice.services.brewing;

import guru.sfg.brewery.model.events.BrewBeerEvent;
import guru.springframework.msscbeerservice.config.JmsConfig;
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
        beerRepository.findAll().forEach(beer -> {
            Integer invQOH = beerInventoryService.getOnHandInventory(beer.getId());

            log.debug("Beer {} {} Min OnHand {} Inventory Quantity OnHand {}", beer.getBeerName(), beer.getId(), beer.getMinOnHand(), invQOH);

            if(beer.getMinOnHand() >= invQOH) {
                log.debug("Send Brewing Request for Beer {} {}", beer.getBeerName(), beer.getId());
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
