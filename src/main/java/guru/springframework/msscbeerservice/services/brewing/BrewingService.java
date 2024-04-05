package guru.springframework.msscbeerservice.services.brewing;

import guru.sfg.brewery.model.events.BrewBeerEvent;
import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import io.micrometer.observation.ObservationRegistry;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrewingService extends JmsTemplatePublish {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final BeerMapper beerMapper;

    public BrewingService(ObservationRegistry observationRegistry, ConnectionFactory connectionFactory,
                          BeerRepository beerRepository, BeerInventoryService beerInventoryService, BeerMapper beerMapper,
                          MessageConverter messageConverter) {
        super(observationRegistry, connectionFactory, messageConverter);
        this.beerRepository = beerRepository;
        this.beerInventoryService = beerInventoryService;
        this.beerMapper = beerMapper;
    }


    @Scheduled(fixedRate = 30000)
    public void checkForLowInventory () {
        beerRepository.findAll().forEach(beer -> {
            Integer invQOH = beerInventoryService.getOnHandInventory(beer.getId());

            log.debug("Beer {} {} Min OnHand {} Inventory Quantity OnHand {}", beer.getBeerName(), beer.getId(), beer.getMinOnHand(), invQOH);

            if(beer.getMinOnHand() >= invQOH) {
                log.debug("Send Brewing Request for Beer {} {}", beer.getBeerName(), beer.getId());
                getJmsTemplate().convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
