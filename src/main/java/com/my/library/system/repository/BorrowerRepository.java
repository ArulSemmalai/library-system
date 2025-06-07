package com.my.library.system.repository;

import com.my.library.system.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, String> {
}
