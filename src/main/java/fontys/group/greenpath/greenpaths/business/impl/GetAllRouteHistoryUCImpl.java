package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.GetAllRouteHistoryUC;
import fontys.group.greenpath.greenpaths.business.converter.RouteHistoryConverter;
import fontys.group.greenpath.greenpaths.domain.responses.GetAllRouteHistoryResponse;
import fontys.group.greenpath.greenpaths.persistence.RouteHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetAllRouteHistoryUCImpl implements GetAllRouteHistoryUC {
    private RouteHistoryRepository routeHistoryRepository;

    @Override
    public GetAllRouteHistoryResponse GetAllRouteHistory(int userId) {
        GetAllRouteHistoryResponse response = GetAllRouteHistoryResponse.builder()
                .routes(
                        routeHistoryRepository.getAllByUserId(userId)
                                .stream()
                                .map(RouteHistoryConverter::convert)
                                .toList()
                )
                .build();

        return response;
    }
}
