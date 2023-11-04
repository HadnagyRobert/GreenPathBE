package fontys.group.greenpath.greenpaths.Scheduler;

import fontys.group.greenpath.greenpaths.business.DataFetcher;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FetchSensorApiData {
    private List<DataFetcher> dataFetchers;

    @SneakyThrows
    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void fetchSensorApiData() {
        for (DataFetcher dataFetcher : dataFetchers) {
            dataFetcher.fetchSensorApiData();
        }
    }
}
