package com.inditex.prices.infrastructure.adapters.outbound.database.productprices;

import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.model.ProductPriceJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface ProductPriceJPARepository extends JpaRepository<ProductPriceJPAEntity, Integer> {

    @Query("""
            SELECT pprice FROM ProductPriceJPAEntity pprice WHERE
                pprice.brandId = :brandId
                AND pprice.productId = :productId
                AND pprice.startDate <= :date
                AND pprice.endDate >= :date
                ORDER BY pprice.priority DESC LIMIT 1
           """)
    Optional<ProductPriceJPAEntity> findAppliedProductPriceByBrandAndProductAndDate(
            final Integer brandId,
            final Integer productId,
            final ZonedDateTime date
    );

}
