package ru.yandex.project.service.participationrequest.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.project.service.participationrequest.model.ParticipationRequest;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(Long userId);

    List<ParticipationRequest> findAllByRequesterAndEvent(Long userId, Long eventId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ParticipationRequest pr SET pr.status = ?1 WHERE pr.event = ?2")
    void changeStatusOfPendingRequestsToRejected(Long eventId, String status);

    List<ParticipationRequest> findAllByIdNotNull();
}
