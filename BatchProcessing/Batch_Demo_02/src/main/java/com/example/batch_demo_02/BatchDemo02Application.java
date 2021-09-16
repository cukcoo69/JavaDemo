package com.example.batch_demo_02;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@SpringBootApplication
@EnableScheduling
public class BatchDemo02Application {

    @Autowired
    public JobLauncher jobLauncher;

    @Autowired
    public Job job;

    @Scheduled(cron = "0 */1 * * * ?")
    public BatchStatus run() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(new Date().getTime()));
        // We are calling a job with name is defined in application file and jobParameters is 1 value => if we call this
        // end-point again, it'll throw exception due to a job instance is difference from other by NAME and JOB PARAMETERS
        JobParameters jobParameters = new JobParameters(maps);
        JobExecution jobExecution =  jobLauncher.run(job, jobParameters);

        System.out.println("Batch is running!!!");
        while (jobExecution.isRunning()){
            System.out.println("....");
        }
        System.out.println("Batch completed!");

        return jobExecution.getStatus();
    }

    public static void main(String[] args) {
        SpringApplication.run(BatchDemo02Application.class, args);
    }

}
