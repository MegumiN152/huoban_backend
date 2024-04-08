package com.hh.yupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.yupao.model.domain.Tag;
import com.hh.yupao.service.TagService;
import com.hh.yupao.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author ybb
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2024-03-11 01:47:52
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




