package org.example.info.client;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.info.exception.RepositoryNotFoundException;
import org.example.info.exception.UserNotFoundException;
import org.example.info.dto.GitHubRepositoryInformation;
import org.example.info.model.Branch;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final RestClient githubRestClient;

    public Set<GitHubRepositoryInformation> getUserRepositories(String username) {
        log.debug("Fetching repositories for user: {}", username);

        try {

            return githubRestClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .body(new ParameterizedTypeReference<LinkedHashSet<GitHubRepositoryInformation>>() {
                    });

        } catch (HttpClientErrorException.NotFound e) {
            log.error("User not found: {}", username, e);
            throw new UserNotFoundException("User not found: " + username);
        } catch (HttpClientErrorException e) {
            log.error("Error while fetching repositories for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Unexpected error occurred while fetching repositories");
        }
    }

        public Set<Branch> getRepositoryBranches (String owner, String repo){
            log.debug("Fetching branches for repository: {}/{}", owner, repo);

            try {
                return githubRestClient.get()
                        .uri("/repos/{owner}/{repo}/branches", owner, repo)
                        .retrieve()
                        .body(new ParameterizedTypeReference<LinkedHashSet<Branch>>() {
                        });

            } catch (HttpClientErrorException.NotFound e) {
                log.warn("Repository not found: {}/{}", owner, repo);
                throw new RepositoryNotFoundException(" Repository not found: " + owner + "/" + repo);
            } catch (HttpClientErrorException e) {
                log.error("Error while fetching branches for repository {}/{}: {}", owner, repo, e.getMessage());
                throw new RuntimeException("Unexpected error occurred while fetching branches");
            }
        }
}
