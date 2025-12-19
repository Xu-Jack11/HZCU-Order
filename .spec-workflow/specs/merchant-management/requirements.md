# Requirements - Merchant Management

## Introduction
Implement merchant management capabilities for the system administrator. This includes adding new merchants (canteens), viewing their administrative accounts, and resetting their passwords. This ensures administrators can onboard new merchants and manage their access.

## Alignment with Product Vision
This feature is essential for system growth and maintenance, allowing administrators to expand the platform's merchant base and provide support for account recovery.

## Requirements

### Requirement 1: Add New Merchant
**User Story:** As a System Administrator, I want to add a new merchant to the platform, so that they can start selling dishes.

#### Acceptance Criteria
1. WHEN the admin clicks "Add Merchant" THEN a form SHALL be displayed to enter canteen details (name, campus, address, etc.) and the initial administrator account details (username, password).
2. IF the username is already taken THEN the system SHALL display an error message.
3. WHEN the form is submitted THEN the system SHALL create both the Canteen entity and the corresponding MerchantAccount entity.

### Requirement 2: View Merchant Accounts
**User Story:** As a System Administrator, I want to view the accounts associated with a merchant, so that I can provide support if they forget their username.

#### Acceptance Criteria
1. WHEN viewing the merchant list THEN the admin SHALL be able to see or click a button to view the associated administrative accounts.
2. The account list SHALL display the username, real name (if available), and status of each account.

### Requirement 3: Reset Merchant Password
**User Story:** As a System Administrator, I want to reset a merchant's password, so that they can regain access if they lose their credentials.

#### Acceptance Criteria
1. WHEN viewing a merchant's account THEN the admin SHALL have an option to reset the password.
2. The admin SHALL be prompted to enter a new password or confirm a generated one.
3. WHEN confirmed THEN the system SHALL update the account's password with a secure hash.

## Non-Functional Requirements

### Code Architecture and Modularity
- **Separation of Concerns**: Admin operations should be clearly separated from merchant/user operations in both frontend and backend.
- **DTO Usage**: Use DTOs for data transfer between frontend and backend to avoid exposing internal entity structures.

### Security
- **Authorization**: All merchant management operations MUST be restricted to users with the 'ADMIN' role.
- **Password Safety**: Passwords MUST be hashed using a strong algorithm (e.g., BCrypt) before storage.

### Usability
- **Feedback**: Provide clear success/error messages for all operations.
- **Validation**: Validate input fields on both frontend and backend.
