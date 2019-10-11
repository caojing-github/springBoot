package cn.huanzi.qch.springbootsecurity.sysmenu.controller;

import cn.huanzi.qch.springbootsecurity.common.controller.*;
import cn.huanzi.qch.springbootsecurity.sysmenu.pojo.SysMenu;
import cn.huanzi.qch.springbootsecurity.sysmenu.service.SysMenuService;
import cn.huanzi.qch.springbootsecurity.sysmenu.vo.SysMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysMenu/")
public class SysMenuController extends CommonController<SysMenuVo, SysMenu, String> {
    @Autowired
    private SysMenuService sysMenuService;
}
