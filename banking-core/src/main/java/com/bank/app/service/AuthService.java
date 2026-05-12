package com.bank.app.service;

import com.bank.app.dto.LoginRequest;
import com.bank.app.entity.Device;
import com.bank.app.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DeviceRepository deviceRepository;
    private final EventService eventService;

    public AuthService(DeviceRepository deviceRepository, EventService eventService) {
        this.deviceRepository = deviceRepository;
        this.eventService = eventService;
    }

    public String login(LoginRequest request) {
        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        Device device = resolveDevice(request.getDeviceId(), request.getDeviceType(), request.getIpAddress());
        eventService.recordEvent("LOGIN_SUCCESS", "username=" + username + ", device=" + device.getDeviceId());

        return "User " + username + " authenticated successfully from device " + device.getDeviceId();
    }

    private Device resolveDevice(String deviceId, String deviceType, String ipAddress) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("deviceId is required for login");
        }

        return deviceRepository.findByDeviceId(deviceId)
                .map(device -> {
                    boolean updated = false;
                    if (deviceType != null && !deviceType.equals(device.getDeviceType())) {
                        device.setDeviceType(deviceType);
                        updated = true;
                    }
                    if (ipAddress != null && !ipAddress.equals(device.getIpAddress())) {
                        device.setIpAddress(ipAddress);
                        updated = true;
                    }
                    return updated ? deviceRepository.save(device) : device;
                })
                .orElseGet(() -> {
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setDeviceType(deviceType == null ? "UNKNOWN" : deviceType);
                    device.setIpAddress(ipAddress);
                    return deviceRepository.save(device);
                });
    }
}
