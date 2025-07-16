package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.dto.ProductDTO;
import lamthoncoding.shop.entity.Product;
import lamthoncoding.shop.entity.ProductAttribute;
import lamthoncoding.shop.entity.ProductImage;
import lamthoncoding.shop.entity.ProductSku;
import lamthoncoding.shop.repository.ProductAttributeRepository;
import lamthoncoding.shop.repository.ProductImageRepository;
import lamthoncoding.shop.repository.ProductRepository;
import lamthoncoding.shop.repository.ProductSkuRepository;
import lamthoncoding.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String UPLOAD_DIR = "C:\\New folder\\product-img";
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductAttributeRepository productAttributeRepository;
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<ProductDTO> getAllProductsPageable(String searchText, int page, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return productRepository.getBySearchTextAndPagination(searchText, pageable);
    }

    @Override
    public void toggleProductStatus(int id) {
        productRepository.updateProductStatus(id);
    }

    @Override
    public void updateProduct(Product product, List<MultipartFile> newImages, List<Integer> deleteImageIds) {
        Product existing = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật thông tin cơ bản
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setSummary(product.getSummary());
        existing.setStatus(product.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        // Xử lý xoá ảnh
        if (deleteImageIds != null) {
            List<ProductImage> toRemove = existing.getImages().stream()
                    .filter(img -> deleteImageIds.contains(img.getId()))
                    .collect(Collectors.toList());

            toRemove.forEach(img -> {
                existing.getImages().remove(img);
                productImageRepository.delete(img);
            });
        }

        // Thêm ảnh mới
        for (MultipartFile file : newImages) {
            if (!file.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa có

                    Path targetPath = uploadPath.resolve(fileName);
                    file.transferTo(targetPath.toFile());

                    ProductImage newImg = new ProductImage();
                    newImg.setImageUrl("/product-img/" + fileName);
                    newImg.setProduct(existing);
                    newImg.setCreatedAt(LocalDateTime.now());
                    newImg.setIsPrimary(false);
                    existing.getImages().add(newImg);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                }
            }
        }


        List<ProductSku> existingSkus = existing.getSkus();

// 1. Xóa các SKU không còn nữa
        existingSkus.removeIf(existingSku ->
                product.getSkus().stream()
                        .noneMatch(incoming -> incoming.getId() != null && incoming.getId().equals(existingSku.getId()))
        );

// 2. Thêm mới hoặc cập nhật
        for (ProductSku incomingSku : product.getSkus()) {
            if (incomingSku.getId() == null) {
                // Thêm mới
                ProductSku newSku = new ProductSku();
                newSku.setSku(incomingSku.getSku());
                newSku.setPrice(incomingSku.getPrice());
                newSku.setQuantity(incomingSku.getQuantity());
                newSku.setStatus(incomingSku.getStatus());
                newSku.setCreatedAt(LocalDateTime.now());
                newSku.setProduct(existing);

                // Gán sizeAttribute nếu có
                String sizeValue = incomingSku.getSizeAttribute() != null ? incomingSku.getSizeAttribute().getValue() : null;
                if (sizeValue != null && !sizeValue.trim().isEmpty()) {
                    ProductAttribute attr = productAttributeRepository
                            .findByTypeAndValue("size", sizeValue)
                            .orElseGet(() -> productAttributeRepository.save(new ProductAttribute(null, "size", sizeValue, LocalDateTime.now(), true)));
                    newSku.setSizeAttribute(attr);
                }

                existingSkus.add(newSku);
            } else {
                // Cập nhật SKU có sẵn
                ProductSku existingSku = existingSkus.stream()
                        .filter(s -> s.getId().equals(incomingSku.getId()))
                        .findFirst()
                        .orElseThrow();

                existingSku.setSku(incomingSku.getSku());
                existingSku.setPrice(incomingSku.getPrice());
                existingSku.setQuantity(incomingSku.getQuantity());
                existingSku.setStatus(incomingSku.getStatus());

                // Cập nhật sizeAttribute nếu có
                String sizeValue = incomingSku.getSizeAttribute() != null ? incomingSku.getSizeAttribute().getValue() : null;
                if (sizeValue != null && !sizeValue.trim().isEmpty()) {
                    ProductAttribute attr = productAttributeRepository
                            .findByTypeAndValue("size", sizeValue)
                            .orElseGet(() -> productAttributeRepository.save(new ProductAttribute(null, "size", sizeValue, LocalDateTime.now(), true)));
                    existingSku.setSizeAttribute(attr);
                }
            }
        }

        productRepository.save(existing);

    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

    }

    @Override
    public void addProduct(Product product, List<MultipartFile> images) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        boolean hasPrimary = false;
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    Files.createDirectories(uploadPath);
                    Path targetPath = uploadPath.resolve(fileName);
                    file.transferTo(targetPath.toFile());

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/product-img/" + fileName);
                    img.setCreatedAt(LocalDateTime.now());
                    img.setProduct(product);

                    if (!hasPrimary) {
                        img.setIsPrimary(true);
                        product.setCover("/product-img/" + fileName);
                        hasPrimary = true;
                    } else {
                        img.setIsPrimary(false);
                    }

                    productImages.add(img);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
                }
            }
        }
        product.setImages(productImages);
        List<ProductSku> skus = new ArrayList<>();
        for (ProductSku incomingSku : product.getSkus()) {
            if (incomingSku.getId() == null) {
                // Thêm mới
                ProductSku newSku = new ProductSku();
                newSku.setSku(incomingSku.getSku());
                newSku.setPrice(incomingSku.getPrice());
                newSku.setQuantity(incomingSku.getQuantity());
                newSku.setStatus(incomingSku.getStatus());
                newSku.setCreatedAt(LocalDateTime.now());
                newSku.setProduct(product);

                // Gán sizeAttribute nếu có
                String sizeValue = incomingSku.getSizeAttribute() != null ? incomingSku.getSizeAttribute().getValue() : null;
                if (sizeValue != null && !sizeValue.trim().isEmpty()) {
                    ProductAttribute attr = productAttributeRepository
                            .findByTypeAndValue("size", sizeValue)
                            .orElseGet(() -> productAttributeRepository.save(new ProductAttribute(null, "size", sizeValue, LocalDateTime.now(), true)));
                    newSku.setSizeAttribute(attr);
                }

                skus.add(newSku);
            }

        }
        product.setSkus(skus);
        productRepository.save(product);
    }
}
