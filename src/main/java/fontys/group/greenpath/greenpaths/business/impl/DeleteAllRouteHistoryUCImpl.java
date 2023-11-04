package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.DeleteAllRouteHistoryUC;
import fontys.group.greenpath.greenpaths.persistence.RouteHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteAllRouteHistoryUCImpl implements DeleteAllRouteHistoryUC {
    private RouteHistoryRepository routeHistoryRepository;

    @Override
    public void DeleteAllRoutes(int userId) {
//TODO: check if user is owner
        routeHistoryRepository.deleteRouteHistoryEntitiesByUserId(userId);
    }
}
