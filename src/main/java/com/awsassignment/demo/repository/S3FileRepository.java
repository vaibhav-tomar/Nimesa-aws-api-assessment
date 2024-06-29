package com.awsassignment.demo.repository;

import com.awsassignment.demo.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author vaibhav
 * created on 29-06-2024
 */
public interface S3FileRepository extends JpaRepository<S3File, Long> {
    int countByBucketName(String bucketName);

    List<S3File> findAllByBucketNameAndFileNameContaining(String bucketName, String pattern);
}
