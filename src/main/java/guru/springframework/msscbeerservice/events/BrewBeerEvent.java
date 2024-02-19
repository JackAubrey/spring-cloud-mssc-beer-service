package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
