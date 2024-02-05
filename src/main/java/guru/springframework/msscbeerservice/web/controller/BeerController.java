package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(BeerController.BASE_PATH)
@RestController
public class BeerController {
    static final String BASE_PATH = "/api/v1/beer";

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId")UUID beerId) {
        // todo impl
        return ResponseEntity.ok(BeerDto.builder().build());
    }

    @PostMapping
    public ResponseEntity<Void> saveNewBeer(@Validated @RequestBody BeerDto beerDto) {
        // todo impl
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BASE_PATH + "/"); // todo impl

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
        // todo impl

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
