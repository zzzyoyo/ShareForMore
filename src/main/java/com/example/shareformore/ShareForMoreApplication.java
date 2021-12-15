package com.example.shareformore;

import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.User;
import com.example.shareformore.init.InitAPP;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShareForMoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShareForMoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, ColumnRepository columnRepository, TagRepository tagRepository, WorkRepository workRepository) {
        return new InitAPP(userRepository, columnRepository, tagRepository, workRepository);
//        return args -> {
//            if (userRepository.findByName("Admin") == null) {
//                User admin = new User("Admin", "123456");
//                SpecialColumn column = new SpecialColumn(admin);
//                userRepository.save(admin);
//                columnRepository.save(column);
//            } else {
//                System.out.println(userRepository.findByName("Admin").getColumnSet().size());
//            }
//        };
    }
}
