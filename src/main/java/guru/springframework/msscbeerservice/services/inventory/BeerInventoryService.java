package guru.springframework.msscbeerservice.services.inventory;

import java.util.UUID;

/**
 * Created by jt on 2019-06-07.
 */
public interface BeerInventoryService {
    String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";

    Integer getOnHandInventory(UUID beerId);
}
