package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.CartItem;
import lamthoncoding.shop.entity.ProductSku;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.repository.CartItemRepository;
import lamthoncoding.shop.repository.CartRepository;
import lamthoncoding.shop.repository.ProductSkuRepository;
import lamthoncoding.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductSkuRepository skuRepository;
    @Override
    public void addToCart(User user, Integer skuId, int quantity) {
        ProductSku sku = skuRepository.findById(skuId).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductSku().getId().equals(skuId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductSku(sku);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }
    @Override
    public Cart getCart(User user) {
        return cartRepository.findByUser(user).orElse(null);
    }
    @Override
    public void removeItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Override
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
