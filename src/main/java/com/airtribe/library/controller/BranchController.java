package com.airtribe.library.controller;

import com.airtribe.library.domain.Branch;
import com.airtribe.library.service.BranchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody BranchRequest request) {
        Branch branch = branchService.createBranch(request.name, request.location);
        return ResponseEntity.status(HttpStatus.CREATED).body(branch);
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<Branch> getBranch(@PathVariable String branchId) {
        return ResponseEntity.ok(branchService.getBranch(branchId));
    }

    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignBookToBranch(@RequestBody AssignRequest request) {
        branchService.assignBookToBranch(request.isbn, request.branchId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBook(@RequestBody TransferRequest request) {
        branchService.transferBook(request.isbn, request.fromBranchId, request.toBranchId);
        return ResponseEntity.ok().build();
    }

    static class BranchRequest {
        public String name;
        public String location;
    }

    static class AssignRequest {
        public String isbn;
        public String branchId;
    }

    static class TransferRequest {
        public String isbn;
        public String fromBranchId;
        public String toBranchId;
    }
}
