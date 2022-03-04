package com.karoman0584.nfebackend.repository;

import com.karoman0584.nfebackend.domain.Nfe;
import com.karoman0584.nfebackend.domain.ProcessStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NfeRepository extends CrudRepository<Nfe, Integer> {
    List<Nfe> findAllByStatus(ProcessStatus status);
}
