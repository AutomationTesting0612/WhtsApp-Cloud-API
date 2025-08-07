package com.poc.whtsapp.message.controller;

import com.poc.whtsapp.message.entity.Campaign;
import com.poc.whtsapp.message.service.CampaignService;
import com.poc.whtsapp.message.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private SegmentService segmentService;

    @GetMapping("/list")
    public String listCampaigns(Model model) {
        model.addAttribute("campaigns", campaignService.getAllCampaigns());
        return "campaign/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("campaign", new Campaign());
        model.addAttribute("segments", segmentService.getAllSegments());
        return "campaign/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Campaign> campaign = campaignService.getCampaignById(id);
        model.addAttribute("campaign", campaign);
        model.addAttribute("segments", segmentService.getAllSegments());
        return "campaign/form";
    }

    @PostMapping("/save")
    public String saveCampaign(@ModelAttribute Campaign campaign) {
        campaignService.createCampaign(campaign);
        return "redirect:/campaign/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return "redirect:/campaign/list";
    }
}
