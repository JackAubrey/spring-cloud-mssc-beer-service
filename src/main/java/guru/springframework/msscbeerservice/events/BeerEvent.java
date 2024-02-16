package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
@Data
@RequiredArgsConstructor
@Builder
public class BeerEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -1808464653797553140L;

    private final BeerDto beerDto;
}
