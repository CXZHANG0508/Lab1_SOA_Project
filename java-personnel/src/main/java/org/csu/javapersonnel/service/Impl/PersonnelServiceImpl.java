package org.csu.javapersonnel.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.javapersonnel.entity.Personnel;
import org.csu.javapersonnel.mapper.PersonnelMapper;
import org.csu.javapersonnel.service.PersonnelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, Personnel>
        implements PersonnelService
{
    @Override
    public boolean save(Personnel p) {
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return super.save(p);
    }

    @Override
    public boolean updateById(Personnel p) {
        p.setUpdatedAt(LocalDateTime.now());
        return super.updateById(p);
    }
}
