## VShop
<img width="245" alt="Screenshot 2025-04-06 at 13 19 34" src="https://github.com/user-attachments/assets/7f1c27b2-b8b5-4251-bc38-c6a132b2ffa0" />

### Features

* **Authentication System** - Complete login and signup functionality with validation
* **Clean Architecture** - Multi-module structure following clean architecture principles
* **MVVM Pattern** - Reactive UI with ViewModels and state management
* **User Profile Management** - View and manage authenticated user profiles
* **Shopping Cart** - Add/remove products with local persistence
* **Wishlist** - Save favorite products for later
* **Guest Mode** - Continue shopping without authentication
* **Product Catalog** - Browse and search products with filtering
* **Modern UI** - Beautiful Jetpack Compose UI with Material Design 3

### Architecture

VShop follows **Clean Architecture** principles with clear separation of concerns:

- **Domain Layer**: Pure Kotlin business logic, entities, and use cases
- **Data Layer**: Repository implementations, local database, and network calls
- **Presentation Layer**: ViewModels, Compose UI, and navigation

📚 **[See detailed architecture documentation](documentation/ARCHITECTURE.md)**

### Tech Stack

* **Language**: Kotlin
* **Architecture**: Clean Architecture + MVVM
* **UI**: Jetpack Compose + Material Design 3
* **DI**: Hilt
* **Database**: Room
* **Preferences**: DataStore
* **Image Loading**: Coil
* **Navigation**: Navigation Compose
* **Async**: Coroutines + Flow
* **Modularization**: Multi-module structure
* **State Management**: StateFlow + Compose State

### Module Structure

```
├── app/                 # Main application module
├── core/
│   ├── common/         # Shared utilities and constants
│   ├── ui/             # Shared UI components and theme
│   └── resources/      # Shared resources
├── domain/             # Business logic (pure Kotlin)
├── data/               # Data layer implementation
└── feature/            # Feature-specific UI and ViewModels
```

### Demo Credentials
For testing the login functionality, you can use:
* **Email**: demo@vshop.com
* **Password**: demo123

Or create a new account using the signup feature.

### Key Features

#### 🔐 Authentication

- Email/password login with validation
- User registration with comprehensive validation
- Secure session management with DataStore
- Guest mode for non-authenticated users

#### 🛍️ Shopping Experience

- Product browsing and search
- Category filtering
- Add to cart/wishlist functionality
- Shopping cart management

#### 👤 User Profile

- View user information
- Authentication status
- Logout functionality
- Profile management options

#### 🏗️ Architecture Benefits

- **Testable**: Easy unit testing with pure domain layer
- **Maintainable**: Clear separation of concerns
- **Scalable**: Easy to add new features
- **Framework Independent**: Business logic isolated from Android framework

### Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run the project
4. Use demo credentials or create a new account

### Testing

- Run unit tests: `./gradlew test`
- Run instrumented tests: `./gradlew connectedAndroidTest`

### Contributing

1. Follow the existing architecture patterns
2. Add unit tests for new business logic
3. Use the established package structure
4. Follow clean code principles

### Sneak Peek
https://github.com/user-attachments/assets/56b98dc4-a213-43f3-8837-7c4b2e944986

## VShop

<img width="245" alt="Screenshot 2025-04-06 at 13 19 34" src="https://github.com/user-attachments/assets/cf0a4e74-55f3-498e-b3f2-c36e9b821ed5">

A modern Android e-commerce application built with **Clean Architecture** and **Jetpack Compose**.

---

## 🏗️ **Architecture**

VShop follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer (UI)            │
│  • Jetpack Compose • ViewModels • Navigation   │
└──────────────────┬──────────────────────────────┘
                   │ depends on
┌──────────────────▼──────────────────────────────┐
│              Domain Layer (Business Logic)      │
│  • Use Cases • Entities • Repository Interfaces │
│  • NO Framework Dependencies (Pure Kotlin)      │
└──────────────────┬──────────────────────────────┘
                   │ depends on
┌──────────────────▼──────────────────────────────┐
│              Data Layer (Implementation)        │
│  • Repositories • Database • Network • Cache    ���
└─────────────────────────────────────────────────┘
```

### **Key Use Cases**

#### **1. AuthUseCase** (Authentication Operations)

- ✅ Login / Signup with validation
- ✅ Logout
- ✅ Session validation (isUserLoggedIn)
- ✅ Input validation (email, password)

#### **2. UserUseCase** (User Data Operations)

- ✅ Get current user data
- ✅ Update user profile
- ✅ User preferences management
- ✅ Profile completeness calculation

#### **3. ProductUseCase** (Product & Cart Operations)

- ✅ Browse products (all, by ID, by category)
- ✅ Manage favorites
- ✅ Cart operations (add, remove, update quantity)

---

## 📦 **Module Structure**

```
VShop/
├── app/                    # Application module
├── core/
│   ├── common/            # Shared utilities, constants
│   ├── resources/         # Shared resources (colors, strings, icons)
│   └── ui/                # Shared UI components, base classes
├── data/                  # Data layer implementation
│   ├── dao/              # Room DAOs
│   ├── entities/         # Database entities
│   ├── di/               # Dependency injection modules
│   ├── mappers/          # Entity ↔ Domain model mappers
│   └── repositoryImpl/   # Repository implementations
├── domain/                # Domain layer (Pure Kotlin)
│   ├── model/            # Domain entities
│   ├── repository/       # Repository interfaces
│   ├── usecase/          # Business logic use cases
│   └── validation/       # Domain validation logic
└── feature/               # Feature modules (UI)
    ├── auth/             # Login, Signup screens
    ├── home/             # Home screen
    ├── cart/             # Cart screen
    ├── favorite/         # Favorites screen
    └── profile/          # Profile screen
```

---

## 🚀 **Getting Started**

### **Prerequisites**

- Android Studio Hedgehog or later
- JDK 17 or higher
- Android SDK 34

### **Build & Run**

```bash
git clone https://github.com/yourusername/VShop.git
cd VShop
./gradlew build
```

### **Demo Credentials**

For testing the login feature, use these credentials:

- **Email**: `demo@vshop.com`
- **Password**: `demo123`

---

## 🎯 **Key Features**

### **✅ Implemented**

- 🔐 **Complete Authentication System**
    - Login / Signup with validation
    - Session management with DataStore
    - Auto-logout on session expiry

- 👤 **User Profile Management**
    - View user information
    - Profile completeness tracking
    - Logout functionality

- 🛒 **Product Browsing**
    - Product catalog
    - Product details
    - Favorites management

- 🛍️ **Shopping Cart**
    - Add/remove items
    - Quantity management
    - Cart persistence

- 🎨 **Modern UI/UX**
    - Material Design 3
    - Consistent color scheme
    - Beautiful animations

### **🔧 Base Screen System**

- Automatic session handling
- Auto-redirect to login when needed
- Easy access to user data in any screen
- Loading state management

---

## 📚 **Documentation**

- **[ARCHITECTURE.md](documentation/ARCHITECTURE.md)** - Complete architecture guide
- **[USECASE_GUIDE.md](documentation/USECASE_GUIDE.md)** - Use case patterns and best practices
- **[SESSION_MANAGEMENT.md](documentation/SESSION_MANAGEMENT.md)** - Session management system

---

## 🛠️ **Tech Stack**

### **Architecture**

- Clean Architecture
- MVVM Pattern
- Multi-module architecture

### **UI**

- Jetpack Compose
- Material Design 3
- Navigation Component

### **Dependency Injection**

- Hilt

### **Database**

- Room Database
- DataStore (for preferences)

### **Async**

- Kotlin Coroutines
- Flow

### **Code Quality**

- Kotlin DSL
- Domain-driven design
- Separation of concerns

---

## 🎨 **Color Scheme**

```kotlin
paint_01: #292526  // Dark - Primary buttons, links
paint_02: #787676  // Medium Gray - Secondary text, icons
paint_03: #A3A1A2  // Light Gray - Borders
paint_04: #F2F2F2  // Very Light Gray - Card backgrounds
paint_05: #121111  // Very Dark - Primary text
White:    #FFFFFF  // Main background, button text
```

---

## 📱 **Screens**

1. **Onboard Screen** - Welcome with login/signup/guest options
2. **Login Screen** - Email/password authentication
3. **Sign Up Screen** - New user registration
4. **Home Screen** - Product catalog with personalized greeting
5. **Cart Screen** - Shopping cart management
6. **Favorite Screen** - Saved favorite products
7. **Profile Screen** - User profile and logout

---

## 🧪 **Testing**

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

---

## 🤝 **Contributing**

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 **License**

This project is licensed under the MIT License.

---

## 👨‍💻 **Author**

**Vanard**

---

## 🌟 **Highlights**

- ✅ **Production-ready** clean architecture
- ✅ **Scalable** modular structure
- ✅ **Testable** domain layer with no framework dependencies
- ✅ **Maintainable** clear separation of concerns
- ✅ **Modern** latest Android development practices

---

**Built with ❤️ using Kotlin and Jetpack Compose**
