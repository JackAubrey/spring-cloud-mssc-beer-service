package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.web.converters.DateConverter;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateConverter.class}, componentModel = "spring")
public interface BeerMapper {
    /*@Mappings(
            @Mapping(source = "minOnHand", target = "quantityOnHand")
    )*/
    @Mapping(target = "quantityOnHand", ignore = true)
    BeerDto beerToBeerDto(Beer beer);

    @Mapping(target = "minOnHand", ignore = true)
    @Mapping(target = "quantityToBrew", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}
