package be.susu.web.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.susu.service.SusuService;
import be.susu.web.dto.CreateGroupDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final SusuService susuService;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupDto dto, Principal principal) {
        var g = susuService.createGroup(dto, principal.getName());
        return ResponseEntity.ok(g);
    }


    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable UUID groupId) {
        return ResponseEntity.ok(susuService.getMembersForGroup(groupId)); // replace with proper group fetch if needed
    }
}
