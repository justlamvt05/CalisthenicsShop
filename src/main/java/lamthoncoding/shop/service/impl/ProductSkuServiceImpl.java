package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.ProductSku;
import lamthoncoding.shop.repository.ProductSkuRepository;
import lamthoncoding.shop.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {
    @Autowired
    private ProductSkuRepository productSkuRepository;
    @Override
    public ProductSku findProductSkuById(int productId) {
        return productSkuRepository.findById(productId).get();
    }
}
