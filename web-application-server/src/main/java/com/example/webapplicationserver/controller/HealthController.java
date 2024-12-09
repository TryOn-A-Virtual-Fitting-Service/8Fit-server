package com.example.webapplicationserver.controller;

import com.example.webapplicationserver.dto.request.RequestHealthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check API", description = "set of health check endpoints")
public class HealthController {
    @Operation(summary = "Health check", description = "Check if the application is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is running"),
            @ApiResponse(responseCode = "500", description = "Application is not running")
    })
    @GetMapping(("/health"))
    public String healthGet() {
        return "Application is running";
    }


    @PostMapping(("/health"))
    public String healthPost(
            @RequestBody RequestHealthDto requestHealthDto
            ) {
        return "Application is running";
    }


}