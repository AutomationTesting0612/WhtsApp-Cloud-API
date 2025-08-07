package com.poc.whtsapp.message.service;

import com.poc.whtsapp.message.entity.Campaign;
import com.poc.whtsapp.message.entity.UserSegment;
import com.poc.whtsapp.message.repository.CampaignRepository;
import com.poc.whtsapp.message.repository.UserSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }

    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        return campaignRepository.findById(id).map(campaign -> {
            campaign.setName(updatedCampaign.getName());
            campaign.setMessage(updatedCampaign.getMessage());
            campaign.setScheduleTime(updatedCampaign.getScheduleTime());
            campaign.setStatus(updatedCampaign.getStatus());
            campaign.setSegment(updatedCampaign.getSegment());
            return campaignRepository.save(campaign);
        }).orElse(null);
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}
