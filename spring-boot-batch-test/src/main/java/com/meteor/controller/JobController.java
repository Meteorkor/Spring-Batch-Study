package com.meteor.controller;

import com.meteor.dto.Result;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("job")
public class JobController {
    private final Logger logger = LoggerFactory.getLogger(JobController.class);

    //JobLocator로는 Job이 안나오네...
//    private final ListableJobLocator jobLocator;
    private final JobLauncher jobLauncher;
    private final Job rangeTestJob;
    private final List<? extends Job> jobs;
    //BeanName, Job
    private final Map<String,? extends Job> jobMap;


    @GetMapping("list")
    public Result getJobList(String name) throws NoSuchJobException {

//        System.out.println("job : " + jobLocator.getJob("etlJob222"));

        for (Job job : jobs) {
            logger.info("job : {}", job);
         }

        for (Map.Entry<String, ? extends Job> stringEntry : jobMap.entrySet()) {
            logger.info("key : {} , value {}", stringEntry.getKey(), stringEntry.getValue());
        }




        return Result.builder().data(jobMap.keySet().toString()).build();

    }

//    @PostMapping("run")
    @GetMapping("run")
    public Result launch(String jobId,@RequestParam(required = false) Map<String,String> jobParam)
            throws NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


        Job job = jobMap.get(jobId);

        JobParametersBuilder builder = new JobParametersBuilder();

//        builder.addDate("now", new Date());
        logger.info("jobParam.toString() : {}" , jobParam.toString());
        System.out.println();
        for (Map.Entry<String, String> stringStringEntry : jobParam.entrySet()) {
            builder.addString(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        JobParameters jobParamInstance = builder.toJobParameters();

        jobLauncher.run(job, jobParamInstance);
        return Result.builder().data("").build();
    }


}
