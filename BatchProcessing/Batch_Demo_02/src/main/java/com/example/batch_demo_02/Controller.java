package com.example.batch_demo_02;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class Controller {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @GetMapping("run")
    public BatchStatus run() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(1L));
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
}
