package com.vishwa.firstjobapp.job.impl;

import com.vishwa.firstjobapp.company.Company;
import com.vishwa.firstjobapp.company.CompanyRepository; // Import CompanyRepository
import com.vishwa.firstjobapp.job.Job;
import com.vishwa.firstjobapp.job.JobRepository;
import com.vishwa.firstjobapp.job.JobService;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.RequestBody; // Not needed in ServiceImpl

// import java.util.ArrayList; // Not used, can be removed if not needed elsewhere
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository; // Added final keyword for consistency
    private final CompanyRepository companyRepository; // Added final keyword for consistency

    // Modified constructor to inject CompanyRepository
    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    // Changed parameter name from 'id' to 'jobId' for consistency with Job entity
    @Override // Added @Override as it implements JobService.getJobById
    public Job getJobById(Long jobId){
        return jobRepository.findById(jobId).orElse(null);
    }

    @Override
    public void createJob(Job job) {
        // Validate if the associated company exists
        Company company = job.getCompany();
        // FIX: Changed company.getId() to company.getCompanyId()
        if (company != null && company.getCompanyId() != null) {
            // FIX: Changed companyRepository.findById(company.getId()) to companyRepository.findById(company.getCompanyId())
            Optional<Company> existingCompany = companyRepository.findById(company.getCompanyId());
            if (existingCompany.isPresent()) {
                job.setCompany(existingCompany.get()); // Set the managed company entity
                jobRepository.save(job);
            } else {
                // If company ID is provided but doesn't exist, throw an exception
                throw new IllegalArgumentException("Invalid company ID: " + company.getCompanyId());
            }
        } else {
            // If no company ID is provided or company object is null, require a company
            throw new IllegalArgumentException("Company information is required for creating a job.");
        }
    }

    // Changed parameter name from 'id' to 'jobId' for consistency
    @Override
    public boolean deleteJobById(Long jobId) {
        try {
            jobRepository.deleteById(jobId);
            return true;
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.err.println("Error deleting job with ID " + jobId + ": " + e.getMessage());
            return false;
        }
    }

    // Changed parameter name from 'id' to 'jobId' for consistency
    @Override
    public boolean updateJob(Long jobId, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setLocation(updatedJob.getLocation());

            // Handle company update:
            // If updatedJob has a company, validate and associate it
            // FIX: Changed updatedJob.getCompany().getId() to updatedJob.getCompany().getCompanyId()
            if (updatedJob.getCompany() != null && updatedJob.getCompany().getCompanyId() != null) {
                // FIX: Changed companyRepository.findById(updatedJob.getCompany().getId()) to companyRepository.findById(updatedJob.getCompany().getCompanyId())
                Optional<Company> existingCompany = companyRepository.findById(updatedJob.getCompany().getCompanyId());
                if (existingCompany.isPresent()) {
                    job.setCompany(existingCompany.get());
                } else {
                    // If company ID is provided but doesn't exist, throw an exception
                    throw new IllegalArgumentException("Invalid company ID provided for update: " + updatedJob.getCompany().getCompanyId());
                }
            } else {
                // Current logic keeps old company if new one is null/empty.
                // If you want to allow setting company to null, you would explicitly set it:
                // job.setCompany(null);
                // Otherwise, the existing company association will remain unchanged.
            }

            jobRepository.save(job);
            return true;
        }
        return false;
    }
}
