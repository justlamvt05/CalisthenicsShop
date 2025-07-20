package lamthoncoding.shop.service;

import lamthoncoding.shop.dto.ProductDTO;
import lamthoncoding.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Page<ProductDTO> getAllProductsPageable(String searchText, int page, int pageSize,
                                            String sortField, String sortDir);
    void toggleProductStatus(int id);
    void updateProduct(Product product, List<MultipartFile> newImages, List<Integer> deleteImageIds);
    Product getProductById(int id);

    void addProduct(Product product, List<MultipartFile> images);
    List<Product> getTop4Products();
    List<Product> findRandomProducts();

    List<Product> searchProductsByName(String name);
}

