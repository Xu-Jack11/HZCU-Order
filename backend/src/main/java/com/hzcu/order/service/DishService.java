package com.hzcu.order.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.hzcu.order.repository.DishJdbcRepository;

@Service
public class DishService {

  private final DishJdbcRepository dishRepo;

  public DishService(DishJdbcRepository dishRepo) {
    this.dishRepo = dishRepo;
  }

  public void updateDish(long dishId, Map<String, Object> payload) {
    dishRepo.updateDish(dishId, payload);
  }

  public void updateAvailability(long dishId, boolean isAvailable) {
    dishRepo.updateAvailability(dishId, isAvailable);
  }
}
