package com.example.demo.Controllers;

import com.example.demo.Repositorys.Entity.Package;
import com.example.demo.Repositorys.Repository.PackageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin-ui/packages")
@RequiredArgsConstructor
public class PackageResource {

    private final PackageRepository packageRepository;

    private final ObjectMapper objectMapper;

    @GetMapping
    public PagedModel<Package> getAll(@ParameterObject Pageable pageable) {
        Page<Package> packages = packageRepository.findAll(pageable);
        return new PagedModel<>(packages);
    }

    @GetMapping("/{id}")
    public Package getOne(@PathVariable Long id) {
        Optional<Package> packageOptional = packageRepository.findById(id);
        return packageOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Package> getMany(@RequestParam List<Long> ids) {
        return packageRepository.findAllById(ids);
    }


    @PatchMapping("/{id}")
    public Package patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Package package1 = packageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(package1).readValue(patchNode);

        return packageRepository.save(package1);
    }



    @DeleteMapping("/{id}")
    public Package delete(@PathVariable Long id) {
        Package package1 = packageRepository.findById(id).orElse(null);
        if (package1 != null) {
            packageRepository.delete(package1);
        }
        return package1;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        packageRepository.deleteAllById(ids);
    }
}
