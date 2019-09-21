package cn.huanzi.qch.springbootsecurity.sysuserauthority.controller;

import cn.huanzi.qch.springbootsecurity.common.controller.*;
import cn.huanzi.qch.springbootsecurity.sysuserauthority.pojo.SysUserAuthority;
import cn.huanzi.qch.springbootsecurity.sysuserauthority.service.SysUserAuthorityService;
import cn.huanzi.qch.springbootsecurity.sysuserauthority.vo.SysUserAuthorityVo;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/sysUserAuthority/")
public class SysUserAuthorityController extends CommonController<SysUserAuthorityVo, SysUserAuthority, String> {
    @Autowired
    private SysUserAuthorityService sysUserAuthorityService;
}
