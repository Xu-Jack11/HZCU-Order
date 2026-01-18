package com.hzcu.order.service;

import com.hzcu.order.entity.AuditLog;
import com.hzcu.order.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void log(String operatorType, Long operatorId, String action, String path, String changes, String ip) {
        AuditLog log = AuditLog.builder()
                .operatorType(operatorType)
                .operatorId(operatorId)
                .action(action)
                .requestPath(path)
                .changes(changes)
                .ipAddress(ip)
                .build();
        auditLogRepository.save(log);
    }
}
