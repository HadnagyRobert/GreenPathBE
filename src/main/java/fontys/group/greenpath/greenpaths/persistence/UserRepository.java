package fontys.group.greenpath.greenpaths.persistence;

import fontys.group.greenpath.greenpaths.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}
