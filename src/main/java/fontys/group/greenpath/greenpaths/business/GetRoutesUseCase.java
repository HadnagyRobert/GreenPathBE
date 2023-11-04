package fontys.group.greenpath.greenpaths.business;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;

import java.util.List;

public interface GetRoutesUseCase {
    GetRoutesResponse getRoutes(RouteRequest request);
}
