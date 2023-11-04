package fontys.group.greenpath.greenpaths.business.converter;

import fontys.group.greenpath.greenpaths.domain.RouteHistory;
import fontys.group.greenpath.greenpaths.persistence.entity.RouteHistoryEntity;

public class RouteHistoryConverter {
    private RouteHistoryConverter(){};

    public static RouteHistory convert(RouteHistoryEntity rh){
        return RouteHistory.builder()
                .id(rh.getId())
                .userId(rh.getUser().getId())
                .origin(rh.getOrigin())
                .destination(rh.getDestination())
                .length(rh.getLength())
                .duration(rh.getDuration())
                .date(rh.getDate())
                .build();
    }
}
