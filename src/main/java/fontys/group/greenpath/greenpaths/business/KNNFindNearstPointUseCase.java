package fontys.group.greenpath.greenpaths.business;

import com.google.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

public interface KNNFindNearstPointUseCase {
    int classifyPoint(HashMap<Integer, List<LatLng>> groupings, LatLng point, int k);
}
