package fontys.group.greenpath.greenpaths.domain.responses;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.domain.RouteResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoutesResponse {
    private RouteResult[] routes;
    private boolean wereThereEnoughSensors;
}
