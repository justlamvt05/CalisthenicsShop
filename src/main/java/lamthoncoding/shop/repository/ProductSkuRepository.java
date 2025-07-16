package lamthoncoding.shop.repository;

import lamthoncoding.shop.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Integer> {
}
