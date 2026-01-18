package com.hzcu.order.service;

import com.hzcu.order.entity.SystemParam;
import com.hzcu.order.repository.SystemParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    @Autowired
    private SystemParamRepository systemParamRepository;

    public List<SystemParam> getAllParams() {
        return systemParamRepository.findAll();
    }

    public Optional<String> getParamValue(String key) {
        return systemParamRepository.findByParamKey(key)
                .map(SystemParam::getParamValue);
    }

    @Transactional
    public SystemParam updateParam(String key, String value, Long userId) {
        SystemParam param = systemParamRepository.findByParamKey(key)
                .orElse(SystemParam.builder().paramKey(key).build());

        param.setParamValue(value);
        param.setUpdatedBy(userId);

        return systemParamRepository.save(param);
    }
}
