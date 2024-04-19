package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Profile("cloud-local")
@Service
public class BeerInventoryServiceFeignImpl implements BeerInventoryService {
    private final InventoryServiceFeignClient inventoryServiceFeignClient;

    public BeerInventoryServiceFeignImpl(InventoryServiceFeignClient inventoryServiceFeignClient) {
        log.info(">>> Beer Inventory Service Feign Client is Up & Running");
        this.inventoryServiceFeignClient = inventoryServiceFeignClient;
    }

    @Override
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling Inventory Service Feign Client for BeerId: {}", beerId);
        ResponseEntity<List<BeerInventoryDto>> responseEntity = inventoryServiceFeignClient.getOnHandInventory(beerId);

        //sum from inventory list
        Integer onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();

        log.debug("Received from Feign Client | BeerId {} On hand is: {}", beerId, onHand);

        return onHand;
    }
}
