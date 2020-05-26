package edu.whut.cs.jee.mooc.upms.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class  JwtAuthenticationRequest implements Serializable {

    private String username;
    private String password;

}
