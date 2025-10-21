package be.susu.web.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ContributionRequest {

    private BigDecimal amount;

}
