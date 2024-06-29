package com.awsassignment.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author vaibhav
 * created on 29-06-2024
 */

@Data
public class FilesByPatternRequest {
    @NotBlank
    private String bucketName;
    @NotBlank
    private String pattern;
}
