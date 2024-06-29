package com.awsassignment.demo.service;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.awsassignment.demo.awsconfig.AwsEc2Client;
import com.awsassignment.demo.awsconfig.AwsS3Client;
import com.awsassignment.demo.entity.JobData;
import com.awsassignment.demo.entity.S3File;
import com.awsassignment.demo.enums.ServiceName;
import com.awsassignment.demo.repository.JobDataRepository;
import com.awsassignment.demo.repository.S3FileRepository;
import com.awsassignment.demo.request.FilesByPatternRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author vaibhav
 * created on 29-06-2024
 */

@Service
@RequiredArgsConstructor
public class AwsService {
    private final AwsS3Client awsS3Client;
    private final AwsEc2Client awsEc2Client;
    private final JobDataRepository jobDataRepository;
    private final S3FileRepository s3FileRepository;
    private static final Gson GSON = new Gson();
   public String discoverServices(List<ServiceName> serviceNames) {
        String jobId = UUID.randomUUID().toString();
        JobData jobData = new JobData();
        jobData.setJobId(jobId);
        jobData.setStatus("In Progress");
       try {
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (ServiceName service : serviceNames) {
            completableFutures.add(CompletableFuture.supplyAsync(() -> {
                try {
                     discoverService(service, jobData);
                } catch (Exception ex) {
                    jobData.setStatus("Failed");
                }
                return null;
            }, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*5)));
        }

           CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).get();
       } catch (InterruptedException | ExecutionException e) {
           jobData.setStatus("Failed");
       } finally {
           jobDataRepository.save(jobData);
       }
       jobData.setStatus("Success");
        jobDataRepository.save(jobData);
        return jobId;
    }

    private void discoverService(ServiceName serviceName, JobData jobData) {
        if (ServiceName.S3.equals(serviceName)) {
            List<Bucket> buckets = awsS3Client.getS3client().listBuckets();
            String bucketsJson = GSON.toJson(buckets);
            jobData.setBucketData(bucketsJson);
        }
        if (ServiceName.EC2.equals(serviceName)) {
            List<Instance> instances = awsEc2Client.getEc2Client().describeInstances().getReservations().stream()
                    .flatMap(reservation -> reservation.getInstances().stream())
                    .toList();
            String instancesJson = GSON.toJson(instances);
            jobData.setInstanceData(instancesJson);
        }
    }

    public String getJobResult(String jobId) throws Exception {
        JobData jobData = jobDataRepository.findByJobId(jobId);
        if(jobData == null) {
            throw new Exception("Invalid job id");
        }
        return jobData.getStatus();
    }

    public List<String> getDiscoveryResult(ServiceName serviceName) throws Exception {
        JobData jobData = jobDataRepository.findFirstByOrderByIdDesc();
        if(jobData == null) {
            throw new Exception("Job Data not found");
        }
        if(ServiceName.S3.equals(serviceName)) {
            List<Bucket> buckets = GSON.fromJson(jobData.getBucketData(), new TypeToken<List<Bucket>>() {
            }.getType());
            return buckets.stream().map(Bucket::getName).collect(Collectors.toList());
        }
        if(ServiceName.EC2.equals(serviceName)) {
            List<Instance> instances = GSON.fromJson(jobData.getInstanceData(), new TypeToken<List<Instance>>() {
            }.getType());
            return instances.stream().map(Instance::getInstanceId).collect(Collectors.toList());
        }
        return null;
    }

    @Async
    public void getS3BucketObjects(String bucketName) {
        ListObjectsV2Request request = new ListObjectsV2Request();
        request.setBucketName(bucketName);
        ListObjectsV2Result response = awsS3Client.getS3client().listObjectsV2(request);
        List<S3File> files = response.getObjectSummaries().stream().map(s3Object -> {
            S3File s3File = new S3File();
            s3File.setBucketName(bucketName);
            s3File.setFileName(s3Object.getKey());
            s3File.setFileUrl("https://" + bucketName + ".s3.amazonaws.com/" + s3Object.getKey());
            return s3File;
        }).collect(Collectors.toList());
        s3FileRepository.saveAll(files);
        System.out.println("internal " + Thread.currentThread().getName());
    }

    public int getS3BucketObjectsCount(String bucketName) {
        return s3FileRepository.countByBucketName(bucketName);
    }

    public List<String> getFilesByPattern(FilesByPatternRequest filesByPatternRequest) {
        List<S3File> files = s3FileRepository.findAllByBucketNameAndFileNameContaining(
                filesByPatternRequest.getBucketName(),
                filesByPatternRequest.getPattern());
        return files.stream().map(S3File::getFileName).collect(Collectors.toList());
    }
}
