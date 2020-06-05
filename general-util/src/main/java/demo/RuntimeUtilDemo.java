package demo;

import cn.hutool.core.util.RuntimeUtil;
import org.junit.Test;

import java.util.List;

/**
 * 命令行工具-RuntimeUtil https://hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%91%BD%E4%BB%A4%E8%A1%8C%E5%B7%A5%E5%85%B7-RuntimeUtil?id=%e5%91%bd%e4%bb%a4%e8%a1%8c%e5%b7%a5%e5%85%b7-runtimeutil
 *
 * @author CaoJing
 * @date 2020/06/03 15:22
 */
public class RuntimeUtilDemo {

    /**
     * 执行系统命令 execForStr
     */
    @Test
    public void test20200603152252() {
        String str = RuntimeUtil.execForStr("jps -l");
        System.out.println(str);
    }

    /**
     * 执行系统命令 execForStr
     */
    @Test
    public void test20200603152605() {
        List<String> list = RuntimeUtil.execForLines("jps -l");
        System.out.println();
    }
}
