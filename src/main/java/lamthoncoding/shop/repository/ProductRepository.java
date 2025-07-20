package lamthoncoding.shop.repository;

import jakarta.transaction.Transactional;
import lamthoncoding.shop.dto.ProductDTO;
import lamthoncoding.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
        select new lamthoncoding.shop.dto.ProductDTO(p.id,p.name,p.description,p.cover, p.createdAt,p.updatedAt,p.status)
           from Product p
           WHERE (:searchText is null or :searchText ='' or lower(p.name) like concat('%', :searchText, '%'))
    """)
    Page<ProductDTO> getBySearchTextAndPagination(@Param("searchText") String searchText, Pageable pageable);
    @Modifying
    @Transactional
    @Query("Update lamthoncoding.shop.entity.Product p set p.status = case when p.status = true then false else true end where p.id =:id")
    void updateProductStatus(@Param("id") int id);
    @EntityGraph(attributePaths = {"images", "skus", "skus.sizeAttribute"})
    Optional<Product> findById(Integer id);
    @Query("SELECT p FROM Product p WHERE p.status = true ORDER BY p.id ASC")
    List<Product> findTop4(Pageable pageable);
    @Query("SELECT p FROM Product p where p.status=true ORDER BY FUNCTION('NEWID')")
    List<Product> findRandomProducts(Pageable pageable);


    List<Product> findByStatusIsTrue();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.status = true")
    List<Product> searchByNameAndStatusTrue(@Param("keyword") String keyword);
}
