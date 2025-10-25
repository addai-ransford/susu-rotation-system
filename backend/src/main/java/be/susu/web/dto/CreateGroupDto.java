package be.susu.web.dto;

import lombok.Data;

@Data
public class CreateGroupDto {
    private String name;
    private String contributionAmount;
    private Integer maxMembers;
    private String frequency;
}
