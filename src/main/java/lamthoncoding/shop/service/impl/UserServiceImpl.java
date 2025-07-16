package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.Role;
import lamthoncoding.shop.entity.User;
import lamthoncoding.shop.dto.UserDTO;
import lamthoncoding.shop.repository.RoleRepository;
import lamthoncoding.shop.repository.UserRepository;
import lamthoncoding.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateById(User updatedUser) {
        User user = userRepository.findById(updatedUser.getId());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setRole(updatedUser.getRole());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getAvatar() != null && !updatedUser.getAvatar().isEmpty()) {
            user.setAvatar(updatedUser.getAvatar());
        }
        user.setStatus(updatedUser.getStatus());
        userRepository.save(user);
    }


    @Override
    public void saveUser(User user) {
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setBirthOfDate(user.getBirthOfDate());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role customer = roleRepository.findByName("customer");
        if (customer == null) {
            throw new RuntimeException("Role 'customer' not found in database");
        }

        user.setRole(customer);
        user.setStatus(true);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByPhoneNumber(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDTO> users_dto = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setBirthOfDate(user.getBirthOfDate());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setRoleId(user.getRole().getId());
            userDTO.setStatus(user.getStatus());
            users_dto.add(userDTO);
        }
        return users_dto;
    }

    @Override
    public Page<UserDTO> listUsers(Integer roleId, Boolean status, String searchText, int page, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return userRepository.getUserList(roleId,status,searchText,pageable);
    }

    @Override
    public void toggleStatus(int id) {
        userRepository.updateStatus(id);
    }

    @Override
    public Optional<User> findByUsernameAndIdNot(String username, int id) {
        return userRepository.findByUsernameAndIdNot(username,id);
    }

    @Override
    public Optional<User> findByEmailAndIdNot(String email, int id) {
        return userRepository.findByUsernameAndIdNot(email,id);
    }

    @Override
    public Optional<User> findByPhoneNumberAndIdNot(String phone, int id) {
        return userRepository.findByPhoneNumberAndIdNot(phone,id);
    }


}
