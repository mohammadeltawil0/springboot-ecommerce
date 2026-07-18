package com.ecommerce.mel_ecom.respository;

import com.ecommerce.mel_ecom.model.AppRole;
import com.ecommerce.mel_ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}
