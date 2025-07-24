# Banking-System

Banking System Web Application
A secure and user-friendly online banking system developed with Spring Boot and MySQL, featuring role-based user management, transaction handling, and real-time reporting. The application includes dedicated interfaces for customers, managers, and administrators, built with Thymeleaf templates and styled using Bootstrap for a responsive design.

Key Features
User Management: Role-based registration and administration of users (Customer, Manager, Admin) with secure authentication and profile management.

Account Handling: Manage multiple bank accounts with balances, statuses (active, pending, rejected), and transaction histories.

Transaction Processing: Perform fund transfers with audit logging and transactional integrity ensured by Spring's @Transactional support.

Audit Logging: Comprehensive logging of key actions like transactions and approvals, stored in a dedicated audit log table and accessible via admin/manager dashboards.

PDF Reporting: Generate detailed PDF reports for banking activities including user summaries, account statuses, balance totals, and audit logs using OpenPDF.

Pagination and Search: Paginated views for transaction history and user lists to ensure scalable data presentation.

Responsive UI: User-friendly interfaces crafted with Thymeleaf and Bootstrap, optimized for desktop and mobile screens.

Technologies Used
Spring Boot, Spring Security, Spring Data JPA

MySQL relational database

Thymeleaf templating engine

Bootstrap CSS framework

OpenPDF (fork of iText) for PDF generation

Lombok for boilerplate code reduction

