package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.Role;
import lamthoncoding.shop.repository.RoleRepository;
import lamthoncoding.shop.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAllByOrderByNameAsc();
    }
}
