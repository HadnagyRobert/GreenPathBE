package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.SaveRouteHistoryUC;
import fontys.group.greenpath.greenpaths.business.exceptions.UserNotFoundException;
import fontys.group.greenpath.greenpaths.domain.AccessToken;
import fontys.group.greenpath.greenpaths.domain.requests.CreateRouteHistoryRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateRouteHistoryResponse;
import fontys.group.greenpath.greenpaths.persistence.RouteHistoryRepository;
import fontys.group.greenpath.greenpaths.persistence.UserRepository;
import fontys.group.greenpath.greenpaths.persistence.entity.RouteHistoryEntity;
import fontys.group.greenpath.greenpaths.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SaveRouteHistoryUCImpl implements SaveRouteHistoryUC {
    private RouteHistoryRepository routeHistoryRepository;
    private UserRepository userRepository;
    private AccessToken accessToken;
    @Override
    public CreateRouteHistoryResponse saveRoute(CreateRouteHistoryRequest request) {
        Optional<UserEntity> user = userRepository.findById(accessToken.getUserId());

        if (user.isEmpty())
        {
            throw new UserNotFoundException("User not found");
        }

            RouteHistoryEntity route = RouteHistoryEntity.builder()
                    .user(user.get())
                    .origin(request.getOrigin())
                    .destination(request.getDestination())
                    .length(request.getLength())
                    .duration(request.getDuration())
                    .date(request.getDate())
                    .build();

            RouteHistoryEntity newRoute = routeHistoryRepository.save(route);

        return CreateRouteHistoryResponse.builder()
                .routeId(newRoute.getId())
                .build();

    }
}
