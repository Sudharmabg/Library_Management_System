package com.airtribe.library.repository;

import com.airtribe.library.domain.Branch;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BranchRepository {
    private final Map<String, Branch> branches = new ConcurrentHashMap<>();

    public Branch save(Branch branch) {
        branches.put(branch.getBranchId(), branch);
        return branch;
    }

    public Optional<Branch> findById(String branchId) {
        return Optional.ofNullable(branches.get(branchId));
    }

    public List<Branch> findAll() {
        return new ArrayList<>(branches.values());
    }

    public void delete(String branchId) {
        branches.remove(branchId);
    }
}
