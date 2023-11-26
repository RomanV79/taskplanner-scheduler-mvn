package ru.vlasov.taskplannerschedulermvn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vlasov.taskplannerschedulermvn.dto.MessageDto;
import ru.vlasov.taskplannerschedulermvn.entity.AppUser;
import ru.vlasov.taskplannerschedulermvn.entity.Task;
import ru.vlasov.taskplannerschedulermvn.entity.TaskStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    @Value("${app.message.quantity-limit-tasks-for-message}")
    private int quantityLimitTasksForMessage;

    public MessageDto create(AppUser appUser) {
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder bodyBuilder = new StringBuilder();

        long counterNotFinished;
        if ((counterNotFinished = countNotFinishedTask(appUser)) > 0) {
            titleBuilder.append("У вас осталось ")
                    .append(counterNotFinished)
                    .append(" несделанных задач. ");

            bodyBuilder.append("Осталось сделать задачи:\n");

            List<Task> filteredTasks = appUser.getTasks().stream()
                    .filter(task -> task.getTaskStatus().equals(TaskStatus.NEW))
                    .limit(quantityLimitTasksForMessage)
                    .toList();
            filteredTasks.forEach(task ->
                    bodyBuilder.append(" - ")
                            .append(task.getTitle())
                            .append("\n"));
        }

        long counterFinishedYesterday;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if ((counterFinishedYesterday = countFinishedTaskByDay(appUser, yesterday)) > 0) {
            titleBuilder.append(" За вчера вы выполнили ")
                    .append(counterFinishedYesterday)
                    .append(" задач");

            if (!bodyBuilder.isEmpty()) {
                bodyBuilder.append("\n\n");
            }

            bodyBuilder.append("Выполненные задачи:\n");

            List<Task> filteredTask = appUser.getTasks().stream()
                    .filter(task -> task.getFinished().equals(yesterday))
                    .limit(quantityLimitTasksForMessage)
                    .toList();
            filteredTask.forEach(task ->
                    bodyBuilder.append(" - ")
                            .append(task.getTitle())
                            .append("\n"));
        }
        return MessageDto.builder()
                .email(appUser.getEmail())
                .title(titleBuilder.toString().trim())
                .body(bodyBuilder.toString().trim())
                .build();
    }

    private static long countNotFinishedTask(AppUser appUser) {
        return appUser.getTasks().stream().filter(task -> task.getTaskStatus().equals(TaskStatus.NEW)).count();
    }

    private static long countFinishedTaskByDay(AppUser appUser, LocalDate localDate) {
        return appUser.getTasks().stream().filter(task -> (task.getFinished() != null && task.getFinished().equals(localDate))).count();
    }
}
