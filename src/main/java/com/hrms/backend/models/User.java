package com.hrms.backend.models;

import com.hrms.backend.models.enums.Department;
import com.hrms.backend.models.enums.Position;
import com.hrms.backend.models.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection  = "users")
public class User implements UserDetails {

    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String password;

    @Field(write = Field.Write.ALWAYS)
    private String gender;

    private String createdAt;

    private Role role;

    @Field(write = Field.Write.ALWAYS)
    private String waitingCompanyCode;

    @Field(write = Field.Write.ALWAYS)
    private String companyCode;

    @Field(write = Field.Write.ALWAYS)
    private String imageUrl;

    @Field(write = Field.Write.ALWAYS)
    private Position position;

    @Field(write = Field.Write.ALWAYS)
    private Department department;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
