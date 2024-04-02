package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class InventoryServiceFeignClientFailover implements InventoryServiceFeignClient {
    private final InventoryFailoverFeignClient inventoryFailOverFeignClient;

    public InventoryServiceFeignClientFailover(InventoryFailoverFeignClient inventoryFailOverFeignClient) {
        this.inventoryFailOverFeignClient = inventoryFailOverFeignClient;
        log.debug(">>>>>>>>> InventoryServiceFeignClientFailOver U&R");
    }

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(UUID beerId) {
        log.debug("Fallback Get on-hand inventory for Beer: {}", beerId);
        return inventoryFailOverFeignClient.getOnHandInventory();
    }
}
