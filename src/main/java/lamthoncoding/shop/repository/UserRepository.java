package lamthoncoding.shop.repository;


import jakarta.transaction.Transactional;
import lamthoncoding.shop.dto.UserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import lamthoncoding.shop.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    User findByUsername(String username);
    User findByPhoneNumber(String phone);
    User findByEmail(String email);
    @Query("""
    SELECT new lamthoncoding.shop.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.username,
        u.email,
        u.birthOfDate,
        u.phoneNumber,
        r.id,
        u.status
    )
    FROM User u
    LEFT JOIN u.role r
    WHERE (:searchText is null or :searchText ='' or 
    lower(u.email) like concat('%', :searchText, '%') or
    lower(u.firstName) like concat('%', :searchText, '%') or
    lower(u.lastName) like concat('%', :searchText, '%') or
    lower(u.username) like concat('%', :searchText, '%'))
    and (:roleId is null or r.id = :roleId)
    and (:status is  null or u.status = :status)
  
""")
    Page<UserDTO> getUserList(@Param("roleId")Integer roleId, @Param("status")Boolean status,@Param("searchText") String searchText, Pageable pageable);


    @Modifying
    @Transactional
    @Query("Update lamthoncoding.shop.entity.User u set u.status = case when u.status = true then false else true end where u.id =:id")
    void updateStatus(@Param("id") int id);

    Optional<User> findByUsernameAndIdNot(String username, int id);
    Optional<User> findByEmailAndIdNot(String email, int id);
    Optional<User> findByPhoneNumberAndIdNot(String phoneNumber, int id);
}
