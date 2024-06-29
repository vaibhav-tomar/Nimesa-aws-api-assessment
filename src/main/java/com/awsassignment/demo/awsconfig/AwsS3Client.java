package com.awsassignment.demo.awsconfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.awsassignment.demo.properties.CommonAppProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author vaibhav
 * created on 28-06-2024
 */

@RequiredArgsConstructor
@Component
public class AwsS3Client {
    private final CommonAppProperties commonAppProperties;
    private AmazonS3 s3client;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(
                commonAppProperties.getAwsAccessKey(),
                commonAppProperties.getAwsSecretKey()
        );

        this.s3client =  AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    public AmazonS3 getS3client() {
        return this.s3client;
    }
}
