package fontys.group.greenpath.greenpaths.domain;

import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Graph {
    private List<List<LatLng>> layers = new ArrayList<>();

    public void addLayer(List<LatLng> layer) {
        layers.add(layer);
    }

    public List<LatLng> getLayer(int index) {
        return layers.get(index);
    }

    public int getLayerCount() {
        return layers.size();
    }
}
