package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.ResourceNotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, Boolean showInventoryOnHand, PageRequest pageRequest) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if ( StringUtils.hasText(beerName) && beerStyle != null ) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle.name(), pageRequest);
        } else if ( StringUtils.hasText(beerName) && beerStyle == null ) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if ( !StringUtils.hasText(beerName) && beerStyle != null) {
            //search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle.name(), pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if ( Boolean.TRUE.equals(showInventoryOnHand) ){
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .toList(),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .toList(),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements() );
        }

        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        if ( Boolean.TRUE.equals(showInventoryOnHand) ){
            return beerMapper.beerToBeerDtoWithInventory(getBeerEntityById(beerId));
        } else {
            return beerMapper.beerToBeerDto(getBeerEntityById(beerId));
        }
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
        beer.setBeerStyle(beerDto.getBeerStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto( beerRepository.save(beer) );
    }

    @Cacheable(cacheNames = "beerUpcCache", key = "#upc", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getByUpc(String upc, Boolean showInventoryOnHand) {
        Assert.hasText(upc, "The UPC value must not be null or blank");
        if ( Boolean.TRUE.equals(showInventoryOnHand) ){
            return beerMapper.beerToBeerDtoWithInventory(getBeerEntityByUpc(upc));
        } else {
            return beerMapper.beerToBeerDto(getBeerEntityByUpc(upc));
        }
    }

    private Beer getBeerEntityByUpc(String upc) {
        return beerRepository.findByUpc(upc)
                .orElseThrow( () -> new ResourceNotFoundException("Unable to find a beer with UPC: "+upc) );
    }

    private Beer getBeerEntityById(UUID beerId) {
        return beerRepository.findById(beerId)
                .orElseThrow( () -> new ResourceNotFoundException("Unable to find a beer with id: "+beerId) );
    }
}
