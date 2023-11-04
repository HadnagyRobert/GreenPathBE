package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.responses.GetAllRouteHistoryResponse;
import org.springframework.stereotype.Service;

public interface GetAllRouteHistoryUC {
    public GetAllRouteHistoryResponse GetAllRouteHistory(int userId);
}
