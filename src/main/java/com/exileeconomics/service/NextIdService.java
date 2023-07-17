package com.exileeconomics.service;

import com.exileeconomics.entity.NextIdEntity;
import com.exileeconomics.repository.NextIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NextIdService {
    private final NextIdRepository nextIdRepository;

    public NextIdService(@Autowired NextIdRepository nextIdRepository) {
        this.nextIdRepository = nextIdRepository;
    }

    public NextIdEntity findFirstByOrderByCreatedAtDesc()  {
        return nextIdRepository.findFirstByOrderByCreatedAtDesc();
    }

    public NextIdEntity save(NextIdEntity entity){
        return nextIdRepository.save(entity);
    }

    public Iterable<NextIdEntity> findAll() {
        return nextIdRepository.findAll();
    }
}
