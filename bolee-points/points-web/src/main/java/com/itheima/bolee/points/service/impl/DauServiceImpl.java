package com.itheima.bolee.points.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.points.mapper.DauMapper;
import com.itheima.bolee.points.pojo.Dau;
import com.itheima.bolee.points.service.IDauService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description：用户日活跃数服务实现类
 */
@Slf4j
@Service
public class DauServiceImpl extends ServiceImpl<DauMapper, Dau> implements IDauService {

}
