package gts.spring.musicManagement.dto;

import gts.spring.musicManagement.entity.Role;

import java.util.Set;

public record RegisterRequestDTO(String username, String password , Set<Role> roles) {
}
