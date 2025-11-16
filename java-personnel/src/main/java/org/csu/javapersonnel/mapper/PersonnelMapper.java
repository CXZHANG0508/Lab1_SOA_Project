package org.csu.javapersonnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.javapersonnel.entity.Personnel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonnelMapper extends BaseMapper<Personnel> {}