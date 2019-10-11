package cn.huanzi.qch.springbootsecurity.sysauthority.controller;

import cn.huanzi.qch.springbootsecurity.common.controller.*;
import cn.huanzi.qch.springbootsecurity.sysauthority.pojo.SysAuthority;
import cn.huanzi.qch.springbootsecurity.sysauthority.service.SysAuthorityService;
import cn.huanzi.qch.springbootsecurity.sysauthority.vo.SysAuthorityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysAuthority/")
public class SysAuthorityController extends CommonController<SysAuthorityVo, SysAuthority, String> {
    @Autowired
    private SysAuthorityService sysAuthorityService;
}
