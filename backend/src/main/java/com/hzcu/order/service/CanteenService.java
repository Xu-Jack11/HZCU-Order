package com.hzcu.order.service;

import com.hzcu.order.entity.Canteen;
import com.hzcu.order.repository.CanteenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CanteenService {

    @Autowired
    private CanteenRepository canteenRepository;

    @Cacheable(value = "canteens", key = "'active_canteens'")
    public List<Canteen> getActiveCanteens() {
        return canteenRepository.findByStatusOrderBySortOrderAsc(1);
    }

    public List<Canteen> getAllCanteens() {
        return canteenRepository.findAllNotDeleted();
    }

    @Cacheable(value = "canteen", key = "#id")
    public Optional<Canteen> getCanteenById(Long id) {
        return canteenRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = { "canteens", "canteen" }, allEntries = true)
    public Canteen saveCanteen(Canteen canteen) {
        return canteenRepository.save(canteen);
    }
}
