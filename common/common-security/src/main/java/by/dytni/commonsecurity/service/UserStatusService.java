package by.dytni.commonsecurity.service;

public interface UserStatusService {

    boolean isActive(Long userId);

    void deactivate(Long userId);

    void activate(Long userId);

}
