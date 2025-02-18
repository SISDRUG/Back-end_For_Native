package com.example.demo.Repositorys.Repository;

import com.example.demo.Repositorys.Entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long> {
}