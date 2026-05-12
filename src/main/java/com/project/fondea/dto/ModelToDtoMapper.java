package com.project.fondea.dto;

import com.project.fondea.dto.campaign.CampaignDto;
import com.project.fondea.dto.user.UserDto;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.User;

public class ModelToDtoMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static CampaignDto toCampaignDto(Campaign campaign) {
        return CampaignDto.builder()
                .id(campaign.getId())
                .title(campaign.getTitle())
                .description(campaign.getDescription())
                .goalAmount(campaign.getGoalAmount())
                .build();
    }
}
