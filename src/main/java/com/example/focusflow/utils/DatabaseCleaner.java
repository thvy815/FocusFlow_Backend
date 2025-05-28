// package com.example.focusflow.utils;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Component;

// @Component
// public class DatabaseCleaner implements CommandLineRunner {

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     @Override
//     public void run(String... args) {
//         // Tắt kiểm tra khóa ngoại
//         jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

//         // Lấy danh sách tất cả bảng trong schema 'railway'
//         jdbcTemplate.query("SELECT table_name FROM information_schema.tables WHERE table_schema = 'railway'",
//             (rs, rowNum) -> rs.getString("table_name")
//         ).forEach(tableName -> {
//             jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName + ";");
//             System.out.println("Dropped table: " + tableName);
//         });

//         // Bật lại kiểm tra khóa ngoại
//         jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
//     }
// }
