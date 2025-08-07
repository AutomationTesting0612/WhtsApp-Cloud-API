package com.poc.whtsapp.message.repository;

import com.poc.whtsapp.message.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {


}
