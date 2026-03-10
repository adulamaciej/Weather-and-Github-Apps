package org.example.info.service;

import org.example.info.client.GitHubClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.info.dto.GitHubRepositoryInformation;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class GitHubServiceIT {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private GitHubClient gitHubClient;

    @Test
    public void shouldRetrieveRepositoriesForExistingUser() {

        // Given
        String username = "torvalds";

        // When
        Set<GitHubRepositoryInformation> repositories = gitHubService.getUserRepositories(username);

        // Then
        assertThat(repositories).isNotEmpty();

        repositories.forEach(repo -> {
            assertThat(repo.getName()).isNotNull();
            assertThat(repo.getOwner().getLogin()).isEqualTo(username);
            assertThat(repo.getBranches()).isNotNull();
            assertThat(repo.getBranches()).isNotEmpty();
        });
    }

    @Test
    public void shouldFilterOutForkRepositories() {
        // Given
        String username = "torvalds";

        // When
        Set<GitHubRepositoryInformation> repos = gitHubClient.getUserRepositories(username);
        Set<GitHubRepositoryInformation> filteredRepositories = gitHubService.getUserRepositories(username);

        // Then
        Set<String> forkNames = repos.stream()
                .filter(GitHubRepositoryInformation::isFork)
                .map(GitHubRepositoryInformation::getName)
                .collect(Collectors.toSet());

        Set<String> filteredNames = filteredRepositories.stream()
                .map(GitHubRepositoryInformation::getName)
                .collect(Collectors.toSet());

        assertThat(forkNames).isNotEmpty();

        for (String forkName : forkNames) {
            assertThat(filteredNames).doesNotContain(forkName);
        }
    }

    @Test
    public void shouldHandleUsernameWithSpaces() {
        // Given
        String usernameWithSpaces = " torvalds ";

        // When
        Set<GitHubRepositoryInformation> repositories = gitHubService.getUserRepositories(usernameWithSpaces.trim());

        // Then
        assertThat(repositories).isNotEmpty();
        repositories.forEach(repo -> assertThat(repo.getOwner().getLogin()).isEqualTo("torvalds"));
    }
}