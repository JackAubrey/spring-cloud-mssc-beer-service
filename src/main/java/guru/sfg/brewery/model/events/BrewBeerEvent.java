package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
