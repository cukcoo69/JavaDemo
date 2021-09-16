package com.example.batch_demo_02;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableBatchProcessing
public class JobAndStep {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("demo_job_01")
                .start(step01())
                .listener(new LoggerListener())
                .build();
    }

    private Step step01() {
        return this.stepBuilderFactory.get("demo_step_01")
                //here we can call new tasklet() then override the method
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello The World");
                    return RepeatStatus.FINISHED;
                })
                .listener(new LoggerListener())
                .build();
    }

    @Bean
    public Job chunkBasedJob() {
        return this.jobBuilderFactory.get("chunkBasedJob")
                .start(chunkStep())
                .build();
    }
    @Bean
    public Step chunkStep() {
        return this.stepBuilderFactory.get("chunkStep")
                .<String, String>chunk(3)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }
    @Bean
    public ListItemReader<String> itemReader() {
        List<String> items = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            items.add(UUID.randomUUID().toString());
        }
        return new ListItemReader<>(items);
    }
    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String item : items) {
                System.out.println(">> current item = " + item);
            }
        };
    }
}
