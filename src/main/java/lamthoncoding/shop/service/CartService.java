package lamthoncoding.shop.service;

import lamthoncoding.shop.entity.Cart;
import lamthoncoding.shop.entity.User;

public interface CartService {
    void addToCart(User user, Integer skuId, int quantity);

    Cart getCart(User user);

    void removeItem(Integer itemId);

    void clearCart(Cart cart);
}
