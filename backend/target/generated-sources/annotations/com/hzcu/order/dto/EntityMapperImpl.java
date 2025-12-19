package com.hzcu.order.dto;

import com.hzcu.order.entity.Canteen;
import com.hzcu.order.entity.Dish;
import com.hzcu.order.entity.DishCategory;
import com.hzcu.order.entity.MerchantAccount;
import com.hzcu.order.entity.Order;
import com.hzcu.order.entity.OrderItem;
import com.hzcu.order.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-19T12:07:05+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId( user.getUserId() );
        userDTO.setOpenid( user.getOpenid() );
        userDTO.setNickname( user.getNickname() );
        userDTO.setAvatarUrl( user.getAvatarUrl() );
        userDTO.setMobile( user.getMobile() );
        userDTO.setStatus( user.getStatus() );
        userDTO.setLastLoginAt( user.getLastLoginAt() );

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.userId( dto.getUserId() );
        user.openid( dto.getOpenid() );
        user.nickname( dto.getNickname() );
        user.avatarUrl( dto.getAvatarUrl() );
        user.mobile( dto.getMobile() );
        user.status( dto.getStatus() );
        user.lastLoginAt( dto.getLastLoginAt() );

        return user.build();
    }

    @Override
    public CanteenDTO toDto(Canteen canteen) {
        if ( canteen == null ) {
            return null;
        }

        CanteenDTO canteenDTO = new CanteenDTO();

        canteenDTO.setCanteenId( canteen.getCanteenId() );
        canteenDTO.setName( canteen.getName() );
        canteenDTO.setCampus( canteen.getCampus() );
        canteenDTO.setLocation( canteen.getLocation() );
        canteenDTO.setContactPhone( canteen.getContactPhone() );
        canteenDTO.setStatus( canteen.getStatus() );
        canteenDTO.setBusinessHours( canteen.getBusinessHours() );
        canteenDTO.setServiceFeeRate( canteen.getServiceFeeRate() );
        canteenDTO.setRemark( canteen.getRemark() );
        canteenDTO.setImageUrl( canteen.getImageUrl() );

        return canteenDTO;
    }

    @Override
    public Canteen toEntity(CanteenDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Canteen.CanteenBuilder canteen = Canteen.builder();

        canteen.canteenId( dto.getCanteenId() );
        canteen.name( dto.getName() );
        canteen.campus( dto.getCampus() );
        canteen.location( dto.getLocation() );
        canteen.contactPhone( dto.getContactPhone() );
        canteen.status( dto.getStatus() );
        canteen.businessHours( dto.getBusinessHours() );
        canteen.serviceFeeRate( dto.getServiceFeeRate() );
        canteen.remark( dto.getRemark() );
        canteen.imageUrl( dto.getImageUrl() );

        return canteen.build();
    }

    @Override
    public DishDTO toDto(Dish dish) {
        if ( dish == null ) {
            return null;
        }

        DishDTO dishDTO = new DishDTO();

        dishDTO.setCanteenId( dishCanteenCanteenId( dish ) );
        dishDTO.setCategoryId( dishCategoryId( dish ) );
        dishDTO.setCategoryName( dishCategoryName( dish ) );
        dishDTO.setDishId( dish.getDishId() );
        dishDTO.setName( dish.getName() );
        dishDTO.setDescription( dish.getDescription() );
        dishDTO.setCoverImage( dish.getCoverImage() );
        dishDTO.setMonthSales( dish.getMonthSales() );
        dishDTO.setBasePrice( dish.getBasePrice() );
        dishDTO.setStatus( dish.getStatus() );

        return dishDTO;
    }

    @Override
    public Dish toEntity(DishDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Dish.DishBuilder dish = Dish.builder();

        dish.name( dto.getName() );
        dish.description( dto.getDescription() );
        dish.coverImage( dto.getCoverImage() );
        dish.monthSales( dto.getMonthSales() );
        dish.basePrice( dto.getBasePrice() );
        dish.status( dto.getStatus() );

        return dish.build();
    }

    @Override
    public void updateDishFromDto(DishDTO dto, Dish dish) {
        if ( dto == null ) {
            return;
        }

        dish.setName( dto.getName() );
        dish.setDescription( dto.getDescription() );
        dish.setCoverImage( dto.getCoverImage() );
        dish.setMonthSales( dto.getMonthSales() );
        dish.setBasePrice( dto.getBasePrice() );
        dish.setStatus( dto.getStatus() );
    }

    @Override
    public OrderDTO toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setUserId( orderUserUserId( order ) );
        orderDTO.setCanteenId( orderCanteenCanteenId( order ) );
        orderDTO.setOrderId( order.getOrderId() );
        orderDTO.setOrderNo( order.getOrderNo() );
        orderDTO.setStatus( order.getStatus() );
        orderDTO.setDiningMode( order.getDiningMode() );
        orderDTO.setReserveStart( order.getReserveStart() );
        orderDTO.setReserveEnd( order.getReserveEnd() );
        orderDTO.setTotalAmount( order.getTotalAmount() );
        orderDTO.setPackageFee( order.getPackageFee() );
        orderDTO.setDiscountAmount( order.getDiscountAmount() );
        orderDTO.setPaidAmount( order.getPaidAmount() );
        orderDTO.setPaymentMethod( order.getPaymentMethod() );
        orderDTO.setPickupCode( order.getPickupCode() );
        orderDTO.setPickupWindow( order.getPickupWindow() );
        orderDTO.setRemark( order.getRemark() );
        orderDTO.setCreatedAt( order.getCreatedAt() );
        orderDTO.setItems( orderItemListToOrderItemDTOList( order.getItems() ) );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.user( orderDTOToUser( dto ) );
        order.canteen( orderDTOToCanteen( dto ) );
        order.orderId( dto.getOrderId() );
        order.orderNo( dto.getOrderNo() );
        order.status( dto.getStatus() );
        order.diningMode( dto.getDiningMode() );
        order.reserveStart( dto.getReserveStart() );
        order.reserveEnd( dto.getReserveEnd() );
        order.totalAmount( dto.getTotalAmount() );
        order.packageFee( dto.getPackageFee() );
        order.discountAmount( dto.getDiscountAmount() );
        order.paidAmount( dto.getPaidAmount() );
        order.paymentMethod( dto.getPaymentMethod() );
        order.pickupCode( dto.getPickupCode() );
        order.pickupWindow( dto.getPickupWindow() );
        order.remark( dto.getRemark() );
        order.createdAt( dto.getCreatedAt() );
        order.items( orderItemDTOListToOrderItemList( dto.getItems() ) );

        return order.build();
    }

    @Override
    public OrderItemDTO toDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setDishId( orderItemDishDishId( orderItem ) );
        orderItemDTO.setId( orderItem.getId() );
        orderItemDTO.setDishName( orderItem.getDishName() );
        orderItemDTO.setSpecName( orderItem.getSpecName() );
        orderItemDTO.setUnitPrice( orderItem.getUnitPrice() );
        orderItemDTO.setQuantity( orderItem.getQuantity() );
        orderItemDTO.setExtraOptions( orderItem.getExtraOptions() );
        orderItemDTO.setTotalPrice( orderItem.getTotalPrice() );

        return orderItemDTO;
    }

    @Override
    public OrderItem toEntity(OrderItemDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.dish( orderItemDTOToDish( dto ) );
        orderItem.id( dto.getId() );
        orderItem.dishName( dto.getDishName() );
        orderItem.specName( dto.getSpecName() );
        orderItem.unitPrice( dto.getUnitPrice() );
        orderItem.quantity( dto.getQuantity() );
        orderItem.extraOptions( dto.getExtraOptions() );
        orderItem.totalPrice( dto.getTotalPrice() );

        return orderItem.build();
    }

    @Override
    public DishCategoryDTO toDto(DishCategory category) {
        if ( category == null ) {
            return null;
        }

        DishCategoryDTO dishCategoryDTO = new DishCategoryDTO();

        dishCategoryDTO.setCanteenId( categoryCanteenCanteenId( category ) );
        dishCategoryDTO.setId( category.getId() );
        dishCategoryDTO.setName( category.getName() );
        dishCategoryDTO.setSortOrder( category.getSortOrder() );
        dishCategoryDTO.setStatus( category.getStatus() );
        dishCategoryDTO.setCreatedAt( category.getCreatedAt() );

        return dishCategoryDTO;
    }

    @Override
    public DishCategory toEntity(DishCategoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DishCategory.DishCategoryBuilder dishCategory = DishCategory.builder();

        dishCategory.id( dto.getId() );
        dishCategory.name( dto.getName() );
        dishCategory.sortOrder( dto.getSortOrder() );
        dishCategory.status( dto.getStatus() );
        dishCategory.createdAt( dto.getCreatedAt() );

        return dishCategory.build();
    }

    @Override
    public MerchantAccountDTO toDto(MerchantAccount account) {
        if ( account == null ) {
            return null;
        }

        MerchantAccountDTO merchantAccountDTO = new MerchantAccountDTO();

        merchantAccountDTO.setCanteenId( accountCanteenCanteenId( account ) );
        merchantAccountDTO.setMerchantAccountId( account.getMerchantAccountId() );
        merchantAccountDTO.setUsername( account.getUsername() );
        merchantAccountDTO.setRealName( account.getRealName() );
        merchantAccountDTO.setMobile( account.getMobile() );
        merchantAccountDTO.setRole( account.getRole() );
        merchantAccountDTO.setStatus( account.getStatus() );
        merchantAccountDTO.setLastLoginAt( account.getLastLoginAt() );

        return merchantAccountDTO;
    }

    @Override
    public MerchantAccount toEntity(MerchantAccountDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MerchantAccount.MerchantAccountBuilder merchantAccount = MerchantAccount.builder();

        merchantAccount.merchantAccountId( dto.getMerchantAccountId() );
        merchantAccount.username( dto.getUsername() );
        merchantAccount.realName( dto.getRealName() );
        merchantAccount.mobile( dto.getMobile() );
        merchantAccount.role( dto.getRole() );
        merchantAccount.status( dto.getStatus() );
        merchantAccount.lastLoginAt( dto.getLastLoginAt() );

        return merchantAccount.build();
    }

    private Long dishCanteenCanteenId(Dish dish) {
        if ( dish == null ) {
            return null;
        }
        Canteen canteen = dish.getCanteen();
        if ( canteen == null ) {
            return null;
        }
        Long canteenId = canteen.getCanteenId();
        if ( canteenId == null ) {
            return null;
        }
        return canteenId;
    }

    private Long dishCategoryId(Dish dish) {
        if ( dish == null ) {
            return null;
        }
        DishCategory category = dish.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String dishCategoryName(Dish dish) {
        if ( dish == null ) {
            return null;
        }
        DishCategory category = dish.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long orderUserUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        Long userId = user.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }

    private Long orderCanteenCanteenId(Order order) {
        if ( order == null ) {
            return null;
        }
        Canteen canteen = order.getCanteen();
        if ( canteen == null ) {
            return null;
        }
        Long canteenId = canteen.getCanteenId();
        if ( canteenId == null ) {
            return null;
        }
        return canteenId;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toDto( orderItem ) );
        }

        return list1;
    }

    protected User orderDTOToUser(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.userId( orderDTO.getUserId() );

        return user.build();
    }

    protected Canteen orderDTOToCanteen(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Canteen.CanteenBuilder canteen = Canteen.builder();

        canteen.canteenId( orderDTO.getCanteenId() );

        return canteen.build();
    }

    protected List<OrderItem> orderItemDTOListToOrderItemList(List<OrderItemDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemDTO orderItemDTO : list ) {
            list1.add( toEntity( orderItemDTO ) );
        }

        return list1;
    }

    private Long orderItemDishDishId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Dish dish = orderItem.getDish();
        if ( dish == null ) {
            return null;
        }
        Long dishId = dish.getDishId();
        if ( dishId == null ) {
            return null;
        }
        return dishId;
    }

    protected Dish orderItemDTOToDish(OrderItemDTO orderItemDTO) {
        if ( orderItemDTO == null ) {
            return null;
        }

        Dish.DishBuilder dish = Dish.builder();

        dish.dishId( orderItemDTO.getDishId() );

        return dish.build();
    }

    private Long categoryCanteenCanteenId(DishCategory dishCategory) {
        if ( dishCategory == null ) {
            return null;
        }
        Canteen canteen = dishCategory.getCanteen();
        if ( canteen == null ) {
            return null;
        }
        Long canteenId = canteen.getCanteenId();
        if ( canteenId == null ) {
            return null;
        }
        return canteenId;
    }

    private Long accountCanteenCanteenId(MerchantAccount merchantAccount) {
        if ( merchantAccount == null ) {
            return null;
        }
        Canteen canteen = merchantAccount.getCanteen();
        if ( canteen == null ) {
            return null;
        }
        Long canteenId = canteen.getCanteenId();
        if ( canteenId == null ) {
            return null;
        }
        return canteenId;
    }
}
