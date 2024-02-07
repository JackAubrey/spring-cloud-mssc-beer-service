package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getBeerById() throws Exception {
        // given
        given(beerService.getById(any(UUID.class))).willReturn(getBeerDto());

        // when
        mockMvc.perform(get("/api/v1/beer" +"/"+ UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(beerService, times(1)).getById(any(UUID.class));
        verifyNoMoreInteractions(beerService);
    }

    @Test
    void saveNewBeer() throws Exception {
        // given
        BeerDto beerDto = getBeerDto();
        BeerDto savedBeerDto = clone(beerDto).id(UUID.randomUUID()).build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);
        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(savedBeerDto);

        // when
        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        // then
        verify(beerService, times(1)).saveNewBeer(any(BeerDto.class));
        verifyNoMoreInteractions(beerService);
    }

    @Test
    void saveNewBeer_fail_when_id_is_set() throws Exception {
        // given
        BeerDto beerDto = getBeerDto();
        beerDto.setId(UUID.randomUUID());
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        // when
        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value("must be null"));

        // then
        verifyNoInteractions(beerService);
    }

    @Test
    void saveNewBeer_fail_when_name_is_blank() throws Exception {
        // given
        BeerDto beerDto = getBeerDto();
        beerDto.setBeerName(null);
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        // when
        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.beerName").value("The Beer Name is mandatory"));

        // then
        verifyNoInteractions(beerService);
    }

    @Test
    void updateBeerById() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();
        BeerDto beerDto = getBeerDto();
        BeerDto updatedBeerDto = clone(beerDto).id(uuid).build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);
        given(beerService.updateBeer(any(UUID.class), any(BeerDto.class))).willReturn(updatedBeerDto);

        // when
        mockMvc.perform(put("/api/v1/beer" +"/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        verify(beerService, times(1)).updateBeer(any(UUID.class), any(BeerDto.class));
        verifyNoMoreInteractions(beerService);
    }

    private BeerDto getBeerDto() {
        return BeerDto.builder()
                .beerName("Beer1")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .upc(BeerLoader.BEER_1_UPC)
                .price(new BigDecimal("12.95"))
                .quantityOnHand(12)
                .build();
    }

    private BeerDto.BeerDtoBuilder clone(BeerDto beerDto) {
        return BeerDto.builder()
                .id(beerDto.getId())
                .createdDate(beerDto.getCreatedDate())
                .version(beerDto.getVersion())
                .lastModifiedDate(beerDto.getLastModifiedDate())
                .price(beerDto.getPrice())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .beerName(beerDto.getBeerName())
                .beerStyle(beerDto.getBeerStyle())
                .upc(beerDto.getUpc());
    }
}