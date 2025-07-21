package lamthoncoding.shop.service;

import lamthoncoding.shop.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSkuService {
    ProductSku findProductSkuById(int productId);
}
