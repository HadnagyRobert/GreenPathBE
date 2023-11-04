package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.requests.CreateRouteHistoryRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateRouteHistoryResponse;

public interface SaveRouteHistoryUC {
    public CreateRouteHistoryResponse saveRoute(CreateRouteHistoryRequest request);
}
