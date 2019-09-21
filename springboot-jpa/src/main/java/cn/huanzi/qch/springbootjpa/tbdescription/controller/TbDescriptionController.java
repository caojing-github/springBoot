package cn.huanzi.qch.springbootjpa.tbdescription.controller;

import cn.huanzi.qch.springbootjpa.common.controller.*;
import cn.huanzi.qch.springbootjpa.tbdescription.pojo.TbDescription;
import cn.huanzi.qch.springbootjpa.tbdescription.service.TbDescriptionService;
import cn.huanzi.qch.springbootjpa.tbdescription.vo.TbDescriptionVo;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/tbDescription/")
public class TbDescriptionController extends CommonController<TbDescriptionVo, TbDescription, Integer> {
    @Autowired
    private TbDescriptionService tbDescriptionService;
}
