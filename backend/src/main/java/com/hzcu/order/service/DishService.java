package com.hzcu.order.service;

import com.hzcu.order.entity.Dish;
import com.hzcu.order.entity.Canteen;
import com.hzcu.order.entity.DishCategory;
import com.hzcu.order.dto.DishDTO;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.repository.DishRepository;
import com.hzcu.order.repository.DishCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishCategoryRepository dishCategoryRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Cacheable(value = "dishes", key = "'canteen_' + #canteen.canteenId")
    public List<Dish> getDishesByCanteen(Canteen canteen) {
        return dishRepository.findByCanteenAndIsDeleted(canteen, 0);
    }

    @Cacheable(value = "dishes", key = "'category_' + #category.categoryId")
    public List<Dish> getDishesByCategory(DishCategory category) {
        return dishRepository.findByCategoryAndStatusAndIsDeleted(category, 1, 0);
    }

    @Cacheable(value = "dish", key = "#id")
    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "dishes", allEntries = true)
    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = "dishes", allEntries = true)
    public Dish addDish(Canteen canteen, String categoryName, Dish dish) {
        dish.setCanteen(canteen);
        dish.setCategory(resolveCategory(canteen, categoryName));
        dish.setIsDeleted(0);
        if (dish.getStatus() == null)
            dish.setStatus(1);
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(value = { "dishes", "dish" }, allEntries = true)
    public Dish updateDish(Dish existingDish, String categoryName, DishDTO dishDTO) {
        entityMapper.updateDishFromDto(dishDTO, existingDish);
        existingDish.setCategory(resolveCategory(existingDish.getCanteen(), categoryName));
        existingDish.setIsDeleted(0);
        return dishRepository.save(existingDish);
    }

    @Transactional
    @CacheEvict(value = { "dishes", "dish" }, allEntries = true)
    public void deleteDish(Long id) {
        dishRepository.findById(id).ifPresent(dish -> {
            dish.setIsDeleted(1);
            dishRepository.save(dish);
        });
    }

    @Transactional
    @CacheEvict(value = { "dishes", "dish" }, allEntries = true)
    public void updateDishStatus(Long id, Integer status) {
        dishRepository.findById(id).ifPresent(dish -> {
            dish.setStatus(status);
            dishRepository.save(dish);
        });
    }

    @Cacheable(value = "categories", key = "'canteen_' + #canteen.canteenId")
    public List<DishCategory> getCategoriesByCanteen(Canteen canteen) {
        return dishCategoryRepository.findByCanteenOrderBySortOrderAsc(canteen);
    }

    @Transactional
    @CacheEvict(value = { "categories", "dishes" }, allEntries = true)
    public DishCategory saveCategory(DishCategory category) {
        return dishCategoryRepository.save(category);
    }

    @Transactional
    @CacheEvict(value = { "categories", "dishes" }, allEntries = true)
    public void deleteCategory(Long id) {
        dishCategoryRepository.deleteById(id);
    }

    @Transactional
    public DishCategory resolveCategory(Canteen canteen, String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = "其它";
        }
        final String finalName = categoryName;
        DishCategory category = dishCategoryRepository.findByCanteenAndName(canteen, finalName)
                .orElseGet(() -> {
                    DishCategory newCat = DishCategory.builder()
                            .name(finalName)
                            .canteen(canteen)
                            .sortOrder(99)
                            .status(1)
                            .build();
                    return dishCategoryRepository.save(newCat);
                });
        System.out.println("DEBUG: Resolved category: " + category.getName() + " with ID: " + category.getId());
        return category;
    }
}
