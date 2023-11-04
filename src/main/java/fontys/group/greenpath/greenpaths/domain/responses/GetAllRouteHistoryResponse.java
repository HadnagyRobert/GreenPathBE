package fontys.group.greenpath.greenpaths.domain.responses;

import fontys.group.greenpath.greenpaths.domain.RouteHistory;
import fontys.group.greenpath.greenpaths.persistence.entity.RouteHistoryEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllRouteHistoryResponse {
    private List<RouteHistory> routes;
}
