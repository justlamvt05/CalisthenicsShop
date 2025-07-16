package lamthoncoding.shop.dto;

import lamthoncoding.shop.entity.Address;
import lamthoncoding.shop.entity.User;
import lombok.Data;

@Data
public class UserProfileForm {
    private User user;
    private Address address;
}
