package lamthoncoding.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private ProductSku productSku;

    private int quantity;

    public double getSubtotal() {
        return productSku != null ? productSku.getPrice().doubleValue() * quantity : 0;
    }
}
