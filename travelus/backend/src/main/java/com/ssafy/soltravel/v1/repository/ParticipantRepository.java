package com.ssafy.soltravel.v1.repository;

import com.ssafy.soltravel.v1.domain.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Participant findByGeneralAccountId(Long accoutId);

    List<Participant> findAllByGeneralAccountId(Long accoutId);

    @Query("SELECT p.user.userId FROM Participant p WHERE p.generalAccount.id = :generalAccountId")
    List<Long> findUserIdsByGeneralAccountId(@Param("generalAccountId") Long generalAccountId);
}
