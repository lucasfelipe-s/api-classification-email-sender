package br.com.vivo.challengeform.repository;

import br.com.vivo.challengeform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmailIgnoreCase(String email);
}
