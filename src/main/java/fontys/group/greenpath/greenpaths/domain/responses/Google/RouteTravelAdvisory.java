package fontys.group.greenpath.greenpaths.domain.responses.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteTravelAdvisory {
    private TollInfo tollInfo;
    private SpeedReadingInterval speedReadingIntervals;
    private String fuelConsumptionMicroliters;
}
