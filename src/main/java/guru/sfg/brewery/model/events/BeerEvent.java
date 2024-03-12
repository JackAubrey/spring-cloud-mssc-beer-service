package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerDto;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BeerEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -1808464653797553140L;

    private BeerDto beerDto;
}
