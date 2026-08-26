package by.dytni.gateway.service;


public interface JwtService {

    boolean validate(String token);

    Long getUserId(String token);

    String getUserRole(String token);

}