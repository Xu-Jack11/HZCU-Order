package com.hzcu.order.config;

import com.hzcu.order.entity.*;
import com.hzcu.order.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MerchantAccountRepository merchantAccountRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private CanteenRepository canteenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DishCategoryRepository dishCategoryRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting data initialization...");

        // 1. Ensure at least one Canteen exists
        Canteen canteen;
        if (canteenRepository.count() == 0) {
            System.out.println("Creating default canteen...");
            canteen = Canteen.builder()
                    .name("一号食堂")
                    .campus("主校区")
                    .location("一号楼一楼")
                    .status(1)
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .sortOrder(1)
                    .build();
            canteen = canteenRepository.save(canteen);
        } else {
            canteen = canteenRepository.findAll().get(0);
        }

        // 2. Initialize Merchant
        if (merchantAccountRepository.count() == 0) {
            System.out.println("Creating 'merchant/123456'...");
            MerchantAccount merchant = MerchantAccount.builder()
                    .username("merchant")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .realName("及木商家")
                    .role("MERCHANT")
                    .status(1)
                    .canteen(canteen)
                    .createdAt(LocalDateTime.now())
                    .build();
            merchantAccountRepository.save(merchant);
        }

        // 3. Initialize Admin
        if (adminUserRepository.count() == 0) {
            System.out.println("Creating 'admin/123456'...");
            AdminUser admin = AdminUser.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .realName("系统管理员")
                    .status(1)
                    .createdAt(LocalDateTime.now())
                    .build();
            adminUserRepository.save(admin);
        }

        // 4. Initialize Test User
        User testUser;
        if (userRepository.count() == 0) {
            System.out.println("Creating test user...");
            testUser = User.builder()
                    .openid("test-openid-123")
                    .nickname("测试用户")
                    .avatarUrl("https://api.dicebear.com/7.x/avataaars/svg?seed=test")
                    .status(1)
                    .createdAt(LocalDateTime.now())
                    .build();
            testUser = userRepository.save(testUser);
        } else {
            testUser = userRepository.findAll().get(0);
        }

        // 5. Initialize Dish Category
        DishCategory category;
        if (dishCategoryRepository.count() == 0) {
            System.out.println("Creating dish categories...");
            category = DishCategory.builder()
                    .name("招牌主菜")
                    .canteen(canteen)
                    .sortOrder(1)
                    .status(1)
                    .createdAt(LocalDateTime.now())
                    .build();
            category = dishCategoryRepository.save(category);
        } else {
            category = dishCategoryRepository.findAll().get(0);
        }

        // 6. Initialize Dishes
        Dish dish1, dish2;
        if (dishRepository.count() == 0) {
            System.out.println("Creating dishes...");
            dish1 = Dish.builder()
                    .name("红烧肉盖饭")
                    .description("经典口味，肥而不腻")
                    .basePrice(new BigDecimal("18.00"))
                    .canteen(canteen)
                    .category(category)
                    .status(1)
                    .createdAt(LocalDateTime.now())
                    .build();
            dish1 = dishRepository.save(dish1);

            dish2 = Dish.builder()
                    .name("宫保鸡丁盖饭")
                    .description("酸甜适口，鸡肉鲜嫩")
                    .basePrice(new BigDecimal("15.00"))
                    .canteen(canteen)
                    .category(category)
                    .status(1)
                    .createdAt(LocalDateTime.now())
                    .build();
            dish2 = dishRepository.save(dish2);
        } else {
            dish1 = dishRepository.findAll().get(0);
            dish2 = dishRepository.findAll().size() > 1 ? dishRepository.findAll().get(1) : dish1;
        }

        // 7. Initialize Test Orders
        if (orderRepository.count() == 0) {
            System.out.println("Creating test orders...");

            // Order 1: PAID (New)
            createTestOrder(testUser, canteen, dish1, "PAID", "100");

            // Order 2: PREPARING (In Progress)
            createTestOrder(testUser, canteen, dish2, "PREPARING", "101");

            // Order 3: READY_FOR_PICKUP (Waiting)
            createTestOrder(testUser, canteen, dish1, "READY_FOR_PICKUP", "102");

            // Order 4: COMPLETED (For stats)
            createTestOrder(testUser, canteen, dish2, "COMPLETED", "103");
        }

        System.out.println("Data initialization completed.");
    }

    private void createTestOrder(User user, Canteen canteen, Dish dish, String status, String code) {
        Order order = Order.builder()
                .user(user)
                .canteen(canteen)
                .orderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                .status(status)
                .diningMode("DINE_IN")
                .totalAmount(dish.getBasePrice())
                .paidAmount(dish.getBasePrice())
                .paymentMethod("WECHAT")
                .pickupCode(code)
                .createdAt(LocalDateTime.now())
                .build();
        order = orderRepository.save(order);

        OrderItem item = OrderItem.builder()
                .order(order)
                .dish(dish)
                .dishName(dish.getName())
                .unitPrice(dish.getBasePrice())
                .quantity(1)
                .totalPrice(dish.getBasePrice())
                .build();
        orderItemRepository.save(item);
    }
}
