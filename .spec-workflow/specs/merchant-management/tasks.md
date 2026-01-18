# Tasks - Merchant Management

- [x] 1. Create DTOs and update Mapper
  - File: `backend/src/main/java/com/hzcu/order/dto/MerchantAccountDTO.java`, `backend/src/main/java/com/hzcu/order/dto/CreateMerchantRequest.java`, `backend/src/main/java/com/hzcu/order/dto/EntityMapper.java`
  - Define `MerchantAccountDTO` and `CreateMerchantRequest`.
  - Update `EntityMapper` to handle `MerchantAccount` mapping.
  - Purpose: Data transfer between layers.
  - _Leverage: backend/src/main/java/com/hzcu/order/dto/UserDTO.java_
  - _Requirements: 1, 2_
  - _Prompt: Role: Java Backend Developer | Task: Create MerchantAccountDTO and CreateMerchantRequest DTOs. Update EntityMapper.java to include mappings for MerchantAccount. | Restrictions: Use MapStruct for mapping. Follow existing DTO patterns. | Success: DTOs defined, Mapper updated and compiles._

- [x] 2. Implement backend service logic
  - File: `backend/src/main/java/com/hzcu/order/service/AdminService.java`
  - Implement `createMerchant`, `getMerchantAccounts`, and `resetMerchantPassword` in `AdminService`.
  - Purpose: Business logic for merchant management.
  - _Leverage: backend/src/main/java/com/hzcu/order/service/MerchantAccountService.java_
  - _Requirements: 1, 2, 3_
  - _Prompt: Role: Java Backend Developer | Task: Implement createMerchant (transactional, creating both Canteen and MerchantAccount), getMerchantAccounts (by canteenId), and resetMerchantPassword (updating hash) in AdminService. | Restrictions: Ensure password hashing using PasswordEncoder. Use @Transactional for creation. | Success: Service methods implemented with proper logic and security._

- [x] 3. Update AdminController endpoints
  - File: `backend/src/main/java/com/hzcu/order/controller/AdminController.java`
  - Add `POST /merchants`, `GET /merchants/{canteenId}/accounts`, and `PATCH /merchants/accounts/{accountId}/password` endpoints.
  - Purpose: Expose management APIs to frontend.
  - _Leverage: backend/src/main/java/com/hzcu/order/controller/AdminController.java_
  - _Requirements: 1, 2, 3_
  - _Prompt: Role: Java Backend Developer | Task: Add endpoints to AdminController for creating merchants, listing accounts, and resetting passwords. | Restrictions: Use appropriate HTTP methods (POST, GET, PATCH). Ensure @PreAuthorize("hasRole('ADMIN')") is active. | Success: Endpoints functional and documented with Swagger annotations._

- [x] 4. Update frontend API client
  - File: `web/lib/api.ts`
  - Add functions for newly created admin endpoints.
  - Purpose: Allow frontend to call new APIs.
  - _Leverage: web/lib/api.ts_
  - _Requirements: 1, 2, 3_
  - _Prompt: Role: Frontend Developer | Task: Add admin.createMerchant, admin.getMerchantAccounts, and admin.resetMerchantPassword to the api object in web/lib/api.ts. | Restrictions: Follow existing request utility patterns. | Success: API client updated with new functions._

- [x] 5. Implement AddMerchantModal
  - File: `web/app/admin/dashboard/canteens/components/AddMerchantModal.tsx`
  - Create a modal for adding a new merchant and their initial account.
  - Purpose: UI for requirement 1.
  - _Leverage: web/app/admin/dashboard/canteens/page.tsx_
  - _Requirements: 1_
  - _Prompt: Role: React Frontend Developer | Task: Create a modal component for adding a merchant. Should include fields for Canteen (name, campus, address, imageUrl) and MerchantAccount (username, password, realName, mobile). | Restrictions: Use Vanilla CSS (module). Handle loading and error states. | Success: Modal functional, validates input, and calls API correctly._

- [x] 6. Implement MerchantAccountListModal and ResetPassword logic
  - File: `web/app/admin/dashboard/canteens/components/AccountListModal.tsx`
  - Create a modal to view accounts and a way to reset password.
  - Purpose: UI for requirements 2 and 3.
  - _Leverage: web/app/admin/dashboard/canteens/page.tsx_
  - _Requirements: 2, 3_
  - _Prompt: Role: React Frontend Developer | Task: Create a modal to list merchant accounts for a canteen and an action to reset their password. | Restrictions: Use Vanilla CSS. Ensure clear feedback on password reset. | Success: Account list correctly displayed, password reset functional._

- [x] 7. Integrate modals into CanteensPage
  - File: `web/app/admin/dashboard/canteens/page.tsx`
  - Add buttons to trigger the new modals and manage state.
  - Purpose: Complete the admin UI flow.
  - _Leverage: web/app/admin/dashboard/canteens/page.tsx_
  - _Requirements: 1, 2, 3_
  - _Prompt: Role: React Frontend Developer | Task: Update the Canteens management page to include "Add Merchant" button and "View Accounts" action for each merchant row. Integrate the new modals. | Restrictions: Maintain consistent styling with existing page. | Success: Page fully functional with new merchant management features._
