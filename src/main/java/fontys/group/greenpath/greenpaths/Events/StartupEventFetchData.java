package fontys.group.greenpath.greenpaths.Events;

import fontys.group.greenpath.greenpaths.business.DataFetcher;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class StartupEventFetchData implements CommandLineRunner {
    private final List<DataFetcher> dataFetchers;
    @Override
    public void run(String... args) throws Exception {
        for (DataFetcher dataFetcher : dataFetchers) {
            dataFetcher.fetchSensorApiData();
        }
    }
}
