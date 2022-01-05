package com.example.springdatajdbcbugreproduction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class SpringDataJdbcBugReproductionApplication implements ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    @Lazy
    @Autowired
    private SampleEntityRepository sampleEntityRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJdbcBugReproductionApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("init-script.sql"));
        sampleEntityRepository.save(SampleEntity.builder().description("I-am-the-senate").build());
    }

    @Table("SAMPLE_ENTITY")
    @Builder
    @Setter
    @Getter
    static class SampleEntity {
        @Id
        private Long id;
        private String description;
    }
}

interface SampleEntityRepository extends CrudRepository<SpringDataJdbcBugReproductionApplication.SampleEntity, Long> {}