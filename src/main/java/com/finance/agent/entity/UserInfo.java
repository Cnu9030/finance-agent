package com.finance.agent.entity;

import java.util.HashSet;
import java.util.Set;


import com.finance.agent.Enum.Roles;
import com.finance.agent.Enum.Status;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "user_info",
        schema = "Sec",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_info_email",
                        columnNames = "email"
                )
        }
)
@Getter 
@Setter
@AllArgsConstructor 
public class UserInfo extends Audit {

    public UserInfo(String email, String password, Set<Roles> roles, Status status) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.status = status;
    }


    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank 
    @Email 
    private String email;

    @NotBlank 
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @NotEmpty 
    private Set<Roles> roles = new HashSet<>();


    @Enumerated(EnumType.STRING)
    @NotNull 
    private Status status;



}
