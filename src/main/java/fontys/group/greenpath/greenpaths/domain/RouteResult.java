package fontys.group.greenpath.greenpaths.domain;

import com.google.maps.model.DirectionsResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResult {
    private DirectionsResult path;
    private RouteStatistics statistics;
}
