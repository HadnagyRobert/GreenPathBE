package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.EllipseUtils;
import fontys.group.greenpath.greenpaths.business.KNNFindNearstPointUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class KNNFindNearstPointUseCaseImpl implements KNNFindNearstPointUseCase {

    private final EllipseUtils ellipseUtils;

    @Override
    public int classifyPoint(HashMap<Integer, List<LatLng>> groupings, LatLng point, int k) {
        // PriorityQueue that stores (group, point) pairs, ordered by the distance to the point
        PriorityQueue<Map.Entry<Integer, LatLng>> pq = new PriorityQueue<>(
                k, Comparator.comparingDouble(o -> -1 * ellipseUtils.calculateDistance(o.getValue(), point))
        );

        for (Map.Entry<Integer, List<LatLng>> groupEntry : groupings.entrySet()) {
            for (LatLng datum : groupEntry.getValue()) {
                Map.Entry<Integer, LatLng> pair = new AbstractMap.SimpleEntry<>(groupEntry.getKey(), datum);

                if (pq.size() < k) {
                    pq.offer(pair);
                } else if (ellipseUtils.calculateDistance(datum, point) < ellipseUtils.calculateDistance(pq.peek().getValue(), point)) {
                    pq.poll();
                    pq.offer(pair);
                }
            }
        }

        // Count the number of neighbors belonging to each group
        HashMap<Integer, Integer> countMap = new HashMap<>();
        while (!pq.isEmpty()) {
            int groupId = pq.poll().getKey();
            countMap.put(groupId, countMap.getOrDefault(groupId, 0) + 1);
        }

        // Return the group with the most neighbors
        return Collections.max(countMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
