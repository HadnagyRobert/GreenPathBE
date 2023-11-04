package fontys.group.greenpath.greenpaths.persistence;

import fontys.group.greenpath.greenpaths.persistence.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Integer> {
}
