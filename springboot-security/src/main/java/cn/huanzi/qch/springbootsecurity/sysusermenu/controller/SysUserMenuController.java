package cn.huanzi.qch.springbootsecurity.sysusermenu.controller;

import cn.huanzi.qch.springbootsecurity.common.controller.CommonController;
import cn.huanzi.qch.springbootsecurity.sysusermenu.pojo.SysUserMenu;
import cn.huanzi.qch.springbootsecurity.sysusermenu.service.SysUserMenuService;
import cn.huanzi.qch.springbootsecurity.sysusermenu.vo.SysUserMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUserMenu/")
public class SysUserMenuController extends CommonController<SysUserMenuVo, SysUserMenu, String> {
    @Autowired
    private SysUserMenuService sysUserMenuService;
}
