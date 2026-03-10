package org.example.info.service;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.info.client.GitHubClient;
import org.example.info.model.Branch;
import org.example.info.model.Owner;
import org.example.info.dto.GitHubRepositoryInformation;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient githubClient;

    public Set<GitHubRepositoryInformation> getUserRepositories(String username) {
        log.debug("Getting repositories for user: {}", username);

        Set<GitHubRepositoryInformation> repositories = githubClient.getUserRepositories(username);

        Set<GitHubRepositoryInformation> result = repositories.stream()
                .filter(repo -> !repo.isFork())
                .map(this::mapToRepository)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.debug("Returning {} non-fork repositories with branch details for user: {}", result.size(), username);

        return result;
    }


    private GitHubRepositoryInformation mapToRepository(GitHubRepositoryInformation gitHubRepo) {
        String repoName = gitHubRepo.getName();
        Owner owner = gitHubRepo.getOwner();

        log.debug("Mapping repository: {}/{}", owner.getLogin(), repoName);

        Set<Branch> branches = githubClient.getRepositoryBranches(owner.getLogin(), repoName);

        log.debug("Retrieved {} branches for repository: {}/{}", branches.size(), owner.getLogin(), repoName);

        return GitHubRepositoryInformation.builder()
                .name(repoName)
                .owner(owner)
                .branches(branches)
                .build();
    }

}