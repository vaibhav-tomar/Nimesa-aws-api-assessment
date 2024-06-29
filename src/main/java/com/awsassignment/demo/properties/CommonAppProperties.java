package com.awsassignment.demo.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author vaibhav
 * created on 28-06-2024
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("com.myapp")
public class CommonAppProperties {
    @NotBlank
    private String awsAccessKey;
    @NotBlank
    private String awsSecretKey;
}
