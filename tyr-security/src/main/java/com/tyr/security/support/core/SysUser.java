package com.tyr.security.support.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SysUser implements UserDetails {

    private int id;
    private String username;
    private String password;
    private String roles;


    private List<GrantedAuthority> authorities;

    public SysUser(int id, String username, String password, String roles, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    //账号是否没过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //账号是否没被锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //密码是否没过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //账号是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}