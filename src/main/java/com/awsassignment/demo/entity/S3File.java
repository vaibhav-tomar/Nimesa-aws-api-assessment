package com.awsassignment.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author vaibhav
 * created on 29-06-2024
 */

@Data
@Entity
@Table(name = "s3_file")
public class S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String bucketName;
    private String fileName;
    private String fileUrl;
}
