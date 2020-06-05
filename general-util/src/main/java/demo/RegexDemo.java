package demo;

import org.junit.Test;

/**
 * 正则表达式
 *
 * @author CaoJing
 * @date 2020/05/25 11:17
 */
public class RegexDemo {

    /**
     * 贪婪模式去除a标签
     */
    @Test
    public void test20200603104819() {
        String s = "《中华人民共和国物权法》\n" +
            "第一百八十七条　以<a class=\"lawregu_link\" lawregu-detail style=\"\" lawreguid=\"96db3f7b4da26066dca74ae363207894\" lawregutiao=\"第一百八十条\">本法第一百八十条第一款第一项</a>至<a class=\"lawregu_link\" lawregu-detail style=\"\" lawreguid=\"96db3f7b4da26066dca74ae363207894\" lawregutiao=\"第一百八十条\">第三项</a>规定的财产或者第五项规定的正在建造的建筑物抵押的,应当办理抵押登记。抵押权自登记时设立。\n";
        s = s.replaceAll("<a.*?>", "").replaceAll("</a>", "");
        System.out.println(s);
    }

    /**
     * https://hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E6%AD%A3%E5%88%99%E5%B7%A5%E5%85%B7-ReUtil
     */
    @Test
    public void test20200525111731() throws Exception {

    }
}
