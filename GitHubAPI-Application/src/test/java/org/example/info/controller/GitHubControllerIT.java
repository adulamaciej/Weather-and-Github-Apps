package org.example.info.controller;

import org.example.info.dto.GitHubRepositoryInformation;
import org.example.info.exception.UserNotFoundException;
import org.example.info.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Set;


@SpringBootTest
public class GitHubControllerIT {

    @Autowired
    private GitHubController gitHubController;

    @Autowired
    private GitHubService gitHubService;

    @Test
    public void shouldReturnRepositoriesForExistingUser() {
        // Given
        String username = "torvalds";

        // When
        ResponseEntity<Set<GitHubRepositoryInformation>> response = gitHubController.getUserRepositories(username);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotEmpty();

        Set<GitHubRepositoryInformation> serviceResults = gitHubService.getUserRepositories(username);
        assertThat(response.getBody()).hasSameSizeAs(serviceResults);
    }

    @Test
    public void shouldThrow404ForNonExistentUser() {
        // Given
        String username = "thisuserdefinitelydoesnotexist12345678900987654321";

        // When & Then
        assertThrows(UserNotFoundException.class, () -> gitHubController.getUserRepositories(username));
    }
}