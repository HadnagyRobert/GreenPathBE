package fontys.group.greenpath.greenpaths.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataInput {
    private List<SensorData> sensorData;
    private SensorType sensorType;
    private double minValue;
}
