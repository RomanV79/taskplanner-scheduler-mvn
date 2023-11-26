package ru.vlasov.taskplannerschedulermvn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vlasov.taskplannerschedulermvn.entity.AppUser;
import ru.vlasov.taskplannerschedulermvn.entity.TaskStatus;
import ru.vlasov.taskplannerschedulermvn.repository.AppUserRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulerService {

    private final AppUserRepository appUserRepository;
    private final MessageService messageService;
    private final ProducerRabbitService producerRabbitService;

    @Scheduled(fixedRate = 15000) // use this annotation for testing scheduler service
//    @Scheduled(cron = "0 0 1 * * ?")
    public void createReportMessage() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<AppUser> filteredUsers = appUserRepository.findWhereTaskFinishedDateOrTaskStatus(yesterday, TaskStatus.NEW);

        for (AppUser user:filteredUsers) {
            producerRabbitService.send(messageService.create(user));
        }
    }
}
