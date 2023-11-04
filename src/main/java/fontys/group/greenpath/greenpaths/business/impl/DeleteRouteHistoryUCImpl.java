package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.DeleteRouteHistoryUC;
import fontys.group.greenpath.greenpaths.persistence.RouteHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteRouteHistoryUCImpl implements DeleteRouteHistoryUC {
    RouteHistoryRepository routeHistoryRepository;

    @Override
    public void DeleteRoute(int routeId) {
        //TODO: check if user is owner
        routeHistoryRepository.deleteById(routeId);
    }
}
