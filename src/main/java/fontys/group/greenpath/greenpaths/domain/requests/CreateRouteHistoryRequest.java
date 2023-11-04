package fontys.group.greenpath.greenpaths.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CreateRouteHistoryRequest {

    private String origin;
    private String destination;
    private double length;
    private int duration;
    private LocalDate date;
}
