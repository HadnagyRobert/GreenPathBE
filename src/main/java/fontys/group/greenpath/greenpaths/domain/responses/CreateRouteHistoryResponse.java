package fontys.group.greenpath.greenpaths.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateRouteHistoryResponse {
    private int routeId;
}
