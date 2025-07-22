package com.hrms.backend.repositories;

import com.hrms.backend.models.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findByCompanyCodeAndParticipantsContains(String companyCode, String participant);

    List<Meeting> findByCompanyCodeAndOrganizer(String companyCode, String organizer);

    boolean existsByCompanyCodeAndParticipantsContainsAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            String companyCode, String participantId, LocalDateTime end, LocalDateTime start
    );
}
