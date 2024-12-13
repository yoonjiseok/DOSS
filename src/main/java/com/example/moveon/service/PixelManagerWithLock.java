package com.example.moveon.service;


import java.util.concurrent.TimeUnit;

import com.example.moveon.exception.AppException;
import com.example.moveon.exception.ErrorCode;
import com.example.moveon.web.dto.PixelDTO.PixelOccupyRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixelManagerWithLock {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final PixelManager pixelManager;
    private final RedissonClient redissonClient;

    public void occupyPixelWithLock(PixelOccupyRequest pixelOccupyRequest) {
        String lockName = REDISSON_LOCK_PREFIX + pixelOccupyRequest.getX() + pixelOccupyRequest.getY();
        RLock rLock = redissonClient.getLock(lockName);

        long waitTime = 5L;
        long leaseTime = 3L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        try {
            boolean available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            if (!available) {
                throw new AppException(ErrorCode.LOCK_ACQUISITION_ERROR);
            }

            pixelManager.occupyPixel(pixelOccupyRequest);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (rLock != null && rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }
}
