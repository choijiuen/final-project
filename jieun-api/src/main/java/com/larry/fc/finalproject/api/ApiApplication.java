package com.larry.fc.finalproject.api;


import com.larry.fc.finalproject.core.SimpleEntity;
import com.larry.fc.finalproject.core.SimpleEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableJpaAuditing //이거를 붙여줘야 auditing 과 관련된 빈들이 올라온다
@EntityScan("com.larry.fc.finalproject.core")
@EnableJpaRepositories("com.larry.fc.finalproject.core")
@RestController
@SpringBootApplication
public class ApiApplication {

    private final SimpleEntityRepository repository;

    public ApiApplication(SimpleEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<SimpleEntity> findAll(){
        return repository.findAll();
    }

    @PostMapping("/save")
    public SimpleEntity saveOne(){
        final SimpleEntity e = new SimpleEntity();
        e.setName("hello");
        return repository.save(e);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
