package gts.spring.musicManagement.repository;

import gts.spring.musicManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RoleRepository  extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
