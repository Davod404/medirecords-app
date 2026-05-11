package medirecords_ms.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.auth.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
