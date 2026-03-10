package org.example.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.info.model.Branch;
import org.example.info.model.Owner;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepositoryInformation {

    private String name;
    private Owner owner;
    private boolean fork;

    @Builder.Default
    private Set<Branch> branches = new LinkedHashSet<>();
}