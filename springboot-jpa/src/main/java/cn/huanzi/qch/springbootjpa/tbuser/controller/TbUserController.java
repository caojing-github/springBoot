package cn.huanzi.qch.springbootjpa.tbuser.controller;

import cn.huanzi.qch.springbootjpa.common.controller.*;
import cn.huanzi.qch.springbootjpa.tbuser.pojo.TbUser;
import cn.huanzi.qch.springbootjpa.tbuser.service.TbUserService;
import cn.huanzi.qch.springbootjpa.tbuser.vo.TbUserVo;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/tbUser/")
public class TbUserController extends CommonController<TbUserVo, TbUser, Integer> {
    @Autowired
    private TbUserService tbUserService;
}
