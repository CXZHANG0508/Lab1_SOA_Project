package org.csu.javapersonnel.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.javapersonnel.entity.Personnel;
import org.csu.javapersonnel.mapper.PersonnelMapper;
import org.csu.javapersonnel.service.PersonnelService;
import org.springframework.stereotype.Service;

@Service
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, Personnel>
        implements PersonnelService
{}
