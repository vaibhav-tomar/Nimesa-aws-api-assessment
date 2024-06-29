package com.awsassignment.demo.entity;

import jakarta.persistence.*;
import lombok.Data;


/**
 * @author vaibhav
 * created on 29-06-2024
 */


@Data
@Entity
public class JobData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobId;
    @Column(name = "bucket_data", columnDefinition = "json")
    private String bucketData;
    @Column(name = "instance_data", columnDefinition = "json")
    private String instanceData;
    private String status;
}
