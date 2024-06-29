package com.awsassignment.demo.awsconfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.awsassignment.demo.properties.CommonAppProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author vaibhav
 * created on 28-06-2024
 */
@Configuration
@Component
@RequiredArgsConstructor
public class AwsEc2Client {
    private final CommonAppProperties commonAppProperties;
    private AmazonEC2 ec2Client;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(
                commonAppProperties.getAwsAccessKey(),
                commonAppProperties.getAwsSecretKey()
        );
        this.ec2Client =  AmazonEC2ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    public AmazonEC2 getEc2Client() {
        return this.ec2Client;
    }
}
