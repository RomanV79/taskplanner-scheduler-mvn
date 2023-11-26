package ru.vlasov.taskplannerschedulermvn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vlasov.taskplannerschedulermvn.entity.AppUser;
import ru.vlasov.taskplannerschedulermvn.entity.TaskStatus;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u LEFT JOIN u.tasks t WHERE t.finished = :date OR t.taskStatus = :status")
    List<AppUser> findWhereTaskFinishedDateOrTaskStatus(LocalDate date, TaskStatus status);
}
