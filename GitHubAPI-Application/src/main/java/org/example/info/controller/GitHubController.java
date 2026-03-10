package org.example.info.controller;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.info.dto.GitHubRepositoryInformation;
import org.example.info.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService githubService;

    @GetMapping("/{username}")
    public ResponseEntity<Set<GitHubRepositoryInformation>> getUserRepositories(@PathVariable String username) {
        Set<GitHubRepositoryInformation> repositories = githubService.getUserRepositories(username);
        return ResponseEntity.ok(repositories);
    }
}
