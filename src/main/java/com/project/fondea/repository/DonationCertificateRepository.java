package com.project.fondea.repository;

import com.project.fondea.model.DonationCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DonationCertificateRepository extends JpaRepository<DonationCertificate, UUID> {
    List<DonationCertificate> findBySponsorId(UUID sponsorId);

    boolean existsByPledgeId(UUID pledgeId);
}
