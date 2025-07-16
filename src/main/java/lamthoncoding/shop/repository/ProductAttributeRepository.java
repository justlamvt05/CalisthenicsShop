package lamthoncoding.shop.repository;

import lamthoncoding.shop.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

    Optional<ProductAttribute> findByTypeAndValue(String type, String value);
}
