package fontys.group.greenpath.greenpaths.controllers;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.business.ComputeRouteUC;
import fontys.group.greenpath.greenpaths.business.GetRoutesTimeBaseUseCase;
import fontys.group.greenpath.greenpaths.business.GetRoutesUseCase;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.requests.TimeBasedRouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/route")
@AllArgsConstructor
public class RouteController {
    private final GetRoutesUseCase getRoutesUseCase;
    private final GetRoutesTimeBaseUseCase getRoutesTimeBaseUseCase;

    @PostMapping
    public ResponseEntity<GetRoutesResponse> computeRoute(@RequestBody @Valid RouteRequest routeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(getRoutesUseCase.getRoutes(routeRequest));
    }

    @PostMapping("/time")
    public ResponseEntity<GetRoutesResponse> computeRouteTimeBased(@RequestBody @Valid TimeBasedRouteRequest routeRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(getRoutesTimeBaseUseCase.getRoutes(routeRequest));
    }
}
