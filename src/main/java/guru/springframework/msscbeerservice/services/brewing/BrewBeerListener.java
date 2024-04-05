package guru.springframework.msscbeerservice.services.brewing;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.events.BrewBeerEvent;
import guru.sfg.brewery.model.events.NewInventoryEvent;
import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import io.micrometer.observation.ObservationRegistry;
import jakarta.jms.ConnectionFactory;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrewBeerListener extends JmsTemplatePublish {
    private final BeerRepository beerRepository;

    public BrewBeerListener(ObservationRegistry observationRegistry, ConnectionFactory connectionFactory, BeerRepository beerRepository,
                            MessageConverter messageConverter) {
        super(observationRegistry, connectionFactory, messageConverter);
        this.beerRepository = beerRepository;
    }

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.getReferenceById(beerDto.getId());

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        log.info("Brewing Beer {} {} QOH: {}", beer.getBeerName(), beer.getId(), beerDto.getQuantityOnHand());

        NewInventoryEvent inventoryEvent = new NewInventoryEvent(beerDto);
        getJmsTemplate().convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, inventoryEvent);
    }
}
