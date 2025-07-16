package lamthoncoding.shop.repository;

import lamthoncoding.shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    List<Role> findAllByOrderByNameAsc();

    @Override
    Optional<Role> findById(Integer integer);
}
