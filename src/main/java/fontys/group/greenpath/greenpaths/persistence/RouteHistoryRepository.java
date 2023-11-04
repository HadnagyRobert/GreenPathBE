package fontys.group.greenpath.greenpaths.persistence;

import fontys.group.greenpath.greenpaths.persistence.entity.RouteHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteHistoryRepository extends JpaRepository<RouteHistoryEntity, Integer> {
    List<RouteHistoryEntity> getAllByUserId(int userId);

    boolean deleteRouteHistoryEntitiesByUserId(int userId);
}
