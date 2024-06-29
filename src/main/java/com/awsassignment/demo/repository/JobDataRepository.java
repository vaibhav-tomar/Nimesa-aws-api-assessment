package com.awsassignment.demo.repository;

import com.awsassignment.demo.entity.JobData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vaibhav
 * created on 29-06-2024
 */
public interface JobDataRepository extends JpaRepository<JobData, Long> {
    JobData findByJobId(String jobId);

    JobData findFirstByOrderByIdDesc();
}
