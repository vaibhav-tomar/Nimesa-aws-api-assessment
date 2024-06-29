package com.awsassignment.demo.controller;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.s3.model.Bucket;
import com.awsassignment.demo.awsconfig.AwsEc2Client;
import com.awsassignment.demo.awsconfig.AwsS3Client;
import com.awsassignment.demo.enums.ServiceName;
import com.awsassignment.demo.request.FilesByPatternRequest;
import com.awsassignment.demo.service.AwsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author vaibhav
 * created on 28-06-2024
 */
@RestController
@RequiredArgsConstructor
public class AwsController {

    private final AwsService awsService;

    @RequestMapping(method = RequestMethod.POST, value = "/discover")
    public ResponseEntity<String> discoverServices(@RequestBody List<ServiceName> serviceNames) {
        return ResponseEntity.ok(awsService.discoverServices(serviceNames));
    }

    @GetMapping("/job-result/{jobId}")
    public ResponseEntity<String> getJobResult(@PathVariable String jobId) throws Exception {
        return new ResponseEntity<>(awsService.getJobResult(jobId), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/discovery-result/{serviceName}")
    public ResponseEntity<List<String>> getDiscoveryResult(@PathVariable ServiceName serviceName) throws Exception {
        return new ResponseEntity<>(awsService.getDiscoveryResult(serviceName), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/s3-bucket-objects/{bucketName}")
    public ResponseEntity<String> getS3BucketObjects(@PathVariable String bucketName) {
        String jobId = UUID.randomUUID().toString();
        awsService.getS3BucketObjects(bucketName);
        System.out.println("External " + Thread.currentThread().getName());
        return ResponseEntity.ok(jobId);

    }

    @GetMapping("/s3-objects-count/{bucketName}")
    public ResponseEntity<Integer> getS3BucketObjectsCount(@PathVariable String bucketName) {
        return ResponseEntity.ok(awsService.getS3BucketObjectsCount(bucketName));
    }

    @PostMapping("/files-by-pattern")
    public ResponseEntity<List<String>> getFilesByPattern(@RequestBody @Valid FilesByPatternRequest filesByPatternRequest) {
        return ResponseEntity.ok(awsService.getFilesByPattern(filesByPatternRequest));
    }
}
