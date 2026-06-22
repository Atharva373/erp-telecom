package com.atharva.erp_telecom.iam.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // GenerationType.IDENTITY - better suited for MYSQL, but while creation SEQUENCE was used hence going with the same
    private Long userId;

    @Column(name = "user_name",nullable = false, unique = true,length = 100)
    private String userName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "user_first_name",nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name",nullable = false)
    private String userLastName;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "enabled")
    private Boolean enabled = true; // Made the default value as true

    @CreatedDate
    @Column(name = "create_time",nullable = false,updatable = false,insertable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "updated_time",nullable = false,insertable = false)
    private LocalDateTime updatedTime;

    // Reverted back to the older version for setter.
    // (Best practice to use this) --> This is our join table which stores a many-to-many mapping for all the users and corresponding roles.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles_join",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
