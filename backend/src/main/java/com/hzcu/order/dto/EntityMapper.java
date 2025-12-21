package com.hzcu.order.dto;

import com.hzcu.order.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    UserDTO toDto(User user);

    User toEntity(UserDTO dto);

    CanteenDTO toDto(Canteen canteen);

    Canteen toEntity(CanteenDTO dto);

    @Mapping(source = "canteen.canteenId", target = "canteenId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    DishDTO toDto(Dish dish);

    @Mapping(target = "canteen", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dishId", ignore = true)
    Dish toEntity(DishDTO dto);

    @Mapping(target = "canteen", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dishId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDishFromDto(DishDTO dto, @org.mapstruct.MappingTarget Dish dish);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "canteen.canteenId", target = "canteenId")
    @Mapping(source = "canteen.name", target = "canteenName")
    @Mapping(source = "canteen.imageUrl", target = "canteenLogo")
    OrderDTO toDto(Order order);

    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "canteenId", target = "canteen.canteenId")
    Order toEntity(OrderDTO dto);

    @Mapping(source = "dish.dishId", target = "dishId")
    @Mapping(source = "dish.coverImage", target = "dishImage")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(source = "dishId", target = "dish.dishId")
    @Mapping(source = "dishName", target = "dishName")
    OrderItem toEntity(OrderItemDTO dto);

    @Mapping(source = "canteen.canteenId", target = "canteenId")
    DishCategoryDTO toDto(DishCategory category);

    @Mapping(target = "canteen", ignore = true)
    DishCategory toEntity(DishCategoryDTO dto);

    @Mapping(source = "canteen.canteenId", target = "canteenId")
    MerchantAccountDTO toDto(MerchantAccount account);

    @Mapping(target = "canteen", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    MerchantAccount toEntity(MerchantAccountDTO dto);
}
