package fontys.group.greenpath.greenpaths.domain;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RivmSensor {
    private int thingId;
    private int dataStreamId;
    private LatLng location;
    private double value;
}
