package cn.huanzi.qch.springbootsecurity.sysuser.controller;

import cn.huanzi.qch.springbootsecurity.common.controller.*;
import cn.huanzi.qch.springbootsecurity.sysuser.pojo.SysUser;
import cn.huanzi.qch.springbootsecurity.sysuser.service.SysUserService;
import cn.huanzi.qch.springbootsecurity.sysuser.vo.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/sysUser/")
public class SysUserController extends CommonController<SysUserVo, SysUser, String> {
    @Autowired
    private SysUserService sysUserService;
}
