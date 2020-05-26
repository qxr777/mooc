package edu.whut.cs.jee.mooc.upms.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthenticationResponse implements Serializable {

    private final String token;

}
