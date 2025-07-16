package lamthoncoding.shop.service;

import lamthoncoding.shop.dto.UserDTO;
import lamthoncoding.shop.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findById(int id);
    void updateById(User updatedUser);
    void saveUser(User user);
    User findByUsername(String username);
    User findByPhoneNumber(String phone);
    User findByEmail(String email);
    List<UserDTO> findAll();
    Page<UserDTO> listUsers(Integer roleId, Boolean status,
                                    String searchText,
                                    int page, int pageSize,
                                    String sortField, String sortDir);
    void toggleStatus(int id);
    Optional<User> findByUsernameAndIdNot(String username, int id);
    Optional<User> findByEmailAndIdNot(String email, int id);
    Optional<User> findByPhoneNumberAndIdNot(String phone, int id);
}
