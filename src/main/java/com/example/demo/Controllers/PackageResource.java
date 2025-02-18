package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Package;
import com.example.demo.Repositorys.Repository.PackageRepository;
import com.example.demo.Services.PackageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/admin-ui/packages")
@RequiredArgsConstructor
public class PackageResource {

    private final PackageRepository packageRepository;
    private final PackageService packageService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Package> getAll(@ParameterObject Pageable pageable) {
        Page<Package> packages = packageRepository.findAll(pageable);
        return new PagedModel<>(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getOne(@PathVariable Long id) {
        Package _package = packageService.getPackageById(id);
        return ResponseEntity.ok(_package);
    }

    @GetMapping("/by-ids")
    public List<Package> getMany(@RequestParam List<Long> ids) {
        return packageRepository.findAllById(ids);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Package> patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Package _package = packageService.patchPackage(id,patchNode);
        return ResponseEntity.ok(_package);
    }



    @DeleteMapping("/{id}")
    public Package delete(@PathVariable Long id) {
        Package _package = packageRepository.findById(id).orElse(null);
        if (_package != null) {
            packageRepository.delete(_package);
        }
        return _package;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        packageRepository.deleteAllById(ids);
    }
}
