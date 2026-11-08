package com.finance.agent.dtos;

import java.util.Set;

import com.finance.agent.Enum.Roles;
import com.finance.agent.Enum.Status;

public record UserDto(String email, String password, Set<Roles> role, Status status) {

}
