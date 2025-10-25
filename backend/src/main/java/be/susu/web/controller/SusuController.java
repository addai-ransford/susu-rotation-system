package be.susu.web.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Membership;
import be.susu.service.SusuService;
import be.susu.web.dto.ContributionRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/susu")
@RequiredArgsConstructor
public class SusuController {

    private static final Logger log = LoggerFactory.getLogger(SusuController.class);

    private final SusuService susuService;

    /**
     * Join a group using the group ID (sent by admin as a link)
     */
    @PostMapping("/groups/{groupId}/join")
    public ResponseEntity<Membership> joinGroup(@PathVariable UUID groupId, Principal principal) {
        log.info("User {} requested to join group {}", principal.getName(), groupId);

        Membership membership = susuService.addMemberToGroup(groupId, principal.getName());
        log.info("User {} successfully joined group {}", principal.getName(), groupId);

        return ResponseEntity.ok(membership);
    }


    /**
     * Contribute to a group's active cycle
     */
    @PostMapping("/groups/{groupId}/contribute")
    public ResponseEntity<Contribution> contribute(@PathVariable UUID groupId,
                                                   @RequestBody ContributionRequest req,
                                                   Principal principal) {
        Contribution c = susuService.contributeToGroup(groupId, principal.getName(), new BigDecimal(String.valueOf(req.getAmount())));
        return ResponseEntity.ok(c);
    }

    /**
     * List all members of a group
     */
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<List<Membership>> getGroupMembers(@PathVariable UUID groupId) {
        List<Membership> members = susuService.getMembersForGroup(groupId);
        return ResponseEntity.ok(members);
    }
}
