alisthenicsShop – E-Commerce Web Application

CalisthenicsShop is a web-based e-commerce application built with Spring Boot and Thymeleaf, designed to sell calisthenics equipment in a simple and user-friendly way.

This project demonstrates a basic online shopping system where users can browse products, manage a cart, and place orders.

📌 Overview

CalisthenicsShop is developed to practice and showcase:

Spring MVC architecture
Server-side rendering with Thymeleaf
CRUD operations
Session-based shopping cart
Database integration

This project is suitable for:

Java web development learners
Academic assignments
Portfolio demonstration
🚀 Features
👤 User
View product list
View product details
Add products to cart
Update/remove items from cart
Place orders
🛠️ Admin (optional)
Manage products (CRUD)
Manage orders
Manage categories
🧱 Technologies Used
🔹 Backend
Java
Spring Boot
Spring MVC
Spring Data JPA
🔹 Frontend
Thymeleaf
HTML / CSS / JavaScript
🔹 Database
MySQL / SQL Server (configurable)
🔹 Tools
Maven
Git & GitHub
Project Structure
CalisthenicsShop/
    │── src/main/java/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── entity/
    │
    │── src/main/resources/
    │   ├── templates/       # Thymeleaf views
    │   ├── static/          # CSS, JS, images
    │   ├── application.properties
    │
    │── pom.xml
    ⚙️ Installation & Setup
1. Clone the repository
git clone https://github.com/justlamvt05/CalisthenicsShop.git
cd CalisthenicsShop
2. Configure database

Edit application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/calisthenics_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
3. Run the application
mvn spring-boot:run
4. Access the application

Open your browser:

http://localhost:8080

📖 Future Improvements
User authentication & authorization (Spring Security)
Payment integration
Product search & filtering
REST API support
Responsive UI
👨‍💻 Author
Vuong Thanh Lam
GitHub: https://github.com/justlamvt05
