package openmpy.taleswiki.view.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentViewCountScheduler {

    private final DocumentViewBackupProcessor backupProcessor;

    @Scheduled(cron = "0 0 * * * *")
    public void backup() {
        backupProcessor.backup();
    }
}
