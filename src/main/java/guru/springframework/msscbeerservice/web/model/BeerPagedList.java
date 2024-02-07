package guru.springframework.msscbeerservice.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class BeerPagedList extends PageImpl<BeerDto> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerPagedList(
            @JsonProperty("content") List<BeerDto> content,
            @JsonProperty("pageNumber") int pageNumber,
            @JsonProperty("pageSize") int pageSize,
            @JsonProperty("sort") Sort.Direction sort,
            @JsonProperty("total") long total) {
        super(content, PageRequest.of(pageNumber,pageSize, sort), total);
    }

    public BeerPagedList(List<BeerDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BeerPagedList(List<BeerDto> content) {
        super(content);
    }
}
