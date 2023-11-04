package fontys.group.greenpath.greenpaths.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class RouteHistory {
    private int id;
    private int userId;
    private String origin;
    private String destination;
    private double length;
    private int duration;
    private LocalDate date;
}
