package com.vishwa.firstjobapp.job;

import java.util.List;

public interface JobService {
    List<Job> findAll();
    Job getJobById(Long jobId);
    void createJob(Job job);
    boolean deleteJobById(Long jobId);
    boolean updateJob(Long jobId, Job updatedJob);
}
