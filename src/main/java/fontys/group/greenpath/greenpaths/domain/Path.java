package fontys.group.greenpath.greenpaths.domain;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    private List<LatLng> path;
}
