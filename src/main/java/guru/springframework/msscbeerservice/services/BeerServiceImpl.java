package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.ResourceNotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getById(UUID beerId) {
        return beerMapper.beerToBeerDto( getBeerEntityById(beerId) );
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        var beerEntity = beerMapper.beerDtoToBeer(beerDto);
        return beerMapper.beerToBeerDto( beerRepository.save(beerEntity) );
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        var beer = getBeerEntityById(beerId);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto( beerRepository.save(beer) );
    }

    private Beer getBeerEntityById(UUID beerId) {
        return beerRepository.findById(beerId)
                .orElseThrow( () -> new ResourceNotFoundException("Unable to find a beer with id: "+beerId) );
    }
}
