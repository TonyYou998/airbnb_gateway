package com.example.microservicegateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private String role;
    private UUID id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;}
