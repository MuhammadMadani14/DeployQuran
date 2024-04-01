package com.example.quran.response;

import lombok.Data;

import java.util.List;

@Data
public class DetailRoleResponse {
    private String id;
    private String username;
    private String email;
    private String photoPath;
    private List<String> roles;
}
