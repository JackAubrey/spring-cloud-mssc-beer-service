package guru.springframework.msscbeerservice.domain;

import guru.sfg.brewery.model.BeerStyleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Beer {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;
    private String beerName;
    @Enumerated(EnumType.STRING)
    private BeerStyleEnum beerStyle;

    @Column(unique = true)
    private String upc;
    private BigDecimal price;

    private Integer minOnHand;
    private Integer quantityToBrew;
}
