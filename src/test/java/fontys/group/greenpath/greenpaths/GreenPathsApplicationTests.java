package fontys.group.greenpath.greenpaths;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fontys.group.greenpath.greenpaths.business.impl.ComputeRouteUCImpl;
import fontys.group.greenpath.greenpaths.domain.requests.Google.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreenPathsApplicationTests {
	@InjectMocks
	private ComputeRouteUCImpl client;
	@Test
	void contextLoads() {

	}

}
