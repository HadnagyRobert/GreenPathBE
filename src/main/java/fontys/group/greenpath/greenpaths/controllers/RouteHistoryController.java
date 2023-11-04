package fontys.group.greenpath.greenpaths.controllers;

import fontys.group.greenpath.greenpaths.business.DeleteAllRouteHistoryUC;
import fontys.group.greenpath.greenpaths.business.DeleteRouteHistoryUC;
import fontys.group.greenpath.greenpaths.business.GetAllRouteHistoryUC;
import fontys.group.greenpath.greenpaths.business.SaveRouteHistoryUC;
import fontys.group.greenpath.greenpaths.domain.requests.CreateRouteHistoryRequest;
import fontys.group.greenpath.greenpaths.domain.responses.CreateRouteHistoryResponse;
import fontys.group.greenpath.greenpaths.domain.responses.GetAllRouteHistoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile/routes")
@AllArgsConstructor
public class RouteHistoryController {
    GetAllRouteHistoryUC getAllRouteHistoryUC;
    SaveRouteHistoryUC saveRouteHistoryUC;
    DeleteRouteHistoryUC deleteRouteHistoryUC;
    DeleteAllRouteHistoryUC deleteAllRouteHistoryUC;

    @GetMapping("/{userId}")
    public GetAllRouteHistoryResponse getAllUserRouteHistory(@PathVariable int userId){
        return getAllRouteHistoryUC.GetAllRouteHistory(userId);
    }

    @PostMapping
    public CreateRouteHistoryResponse saveRoute(@RequestBody @Valid CreateRouteHistoryRequest r){
            return saveRouteHistoryUC.saveRoute(r);
    }

    @DeleteMapping("/{id}")
    public void DeleteRouteHistory(@PathVariable int id){
        deleteRouteHistoryUC.DeleteRoute(id);
    }

    @DeleteMapping("/All/{userId}")
    public void DeleteAllRouteHistory(@PathVariable int userId){
        deleteAllRouteHistoryUC.DeleteAllRoutes(userId);
    }
}
