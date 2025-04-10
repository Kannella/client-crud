package com.giokanella.clientscrud.repositories;

import com.giokanella.clientscrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
