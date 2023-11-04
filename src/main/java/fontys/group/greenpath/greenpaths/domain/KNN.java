package fontys.group.greenpath.greenpaths.domain;


import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class KNN {
    private final int k;
    private final List<LatLng> data;
}
