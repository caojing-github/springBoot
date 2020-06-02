package demo;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Jsoup
 *
 * @author CaoJing
 * @date 2020/05/08 17:46
 */
public class JsoupDemo {

    /**
     * 提取标签中的元素 https://www.coder.work/article/331440
     */
    @Test
    public void test20200508175016() {
        String html = "<html><head/><body><div class=\"main-content\">" +
            "<div class=\"sub-content\">" +
            "<p>a paragraph <b>with some bold text</b></p>" +
            "Sub content here</div>" +
            "Main content here </div></body></html>";
        Document document = Jsoup.parse(html);
        Elements divs = document.select("div, p, b");
        for (Element div : divs) {
            System.out.println(div.ownText());
        }
    }

    /**
     * 提取审判长
     */
    @Test
    public void test20200508180254() {
        String html = "宁芝刑事裁定书\n" +
            "湖南省长沙市中级人民法院\n" +
            "刑事裁定书\n" +
            "（2018）湘01刑更1708号\n" +
            "罪犯宁芝，女，1994年11月27日出生，汉族，小学文化，湖南省邵东县人。现押湖南省未成年犯管教所服刑。\n" +
            "湖南省长沙市开福区人民法院于2015年12月3日作出（2015）开刑初字第00579号刑事判决，以被告人宁芝犯贩卖毒品罪，判处有期徒刑八年，并处罚金人民币一万元（已缴纳）。刑期自2014年12月25日起至2022年12月24日止。该判决发生法律效力后交付执行。\n" +
            "执行机关湖南省未成年犯管教所于2018年7月5日以该犯在服刑期间确有悔改表现，提出减刑建议书，报送本院审理。长沙市星城地区人民检察院出具检察意见，同意对罪犯宁芝减刑。本院依法组成合议庭，按照《最高人民法院关于减刑、假释案件审理程序的规定》予以公示。并公开开庭审理。本案现已审理终结。\n" +
            "经审理查明，罪犯宁芝在服刑期间，能认罪悔罪，服从管教；认真遵守法律法规及监规，接受教育改造；积极参加思想、文化和职业技术教育；积极参加劳动，努力完成劳动改造任务。在劳动改造中，能服从安排，积极肯干。现有4次表扬，余502分。上述事实，有罪犯认罪悔罪书、罪犯考核奖惩统计台账、罪犯奖惩审核表、罪犯减刑评议书等材料证实。\n" +
            "本院认为，罪犯宁芝在服刑中，能认罪悔罪，接受教育改造，积极完成劳动任务，确有悔改表现，符合法定减刑条件，依照《中华人民共和国刑法》第七十八条、第七十九条之规定，裁定如下：\n" +
            "对罪犯宁芝减去有期徒刑六个月（刑期至2022年6月24日止）。\n" +
            "本裁定送达后即发生法律效力。\n" +
            "<table><tbody><tr><td width=\"01lydyh0125301lydyh01\"><a></a><div>审判长龙显雄</div></td></tr><tr><td width=\"01lydyh0125301lydyh01\"><div>审判员旷学瑛</div></td></tr><tr><td width=\"01lydyh0125301lydyh01\"><div>人民陪审员范可鸣</div></td></tr><tr><td width=\"01lydyh0125301lydyh01\"><div>二○一八年七月十九日</div></td></tr><tr><td width=\"01lydyh0125301lydyh01\"><div>法官助理廖雯娜</div><div>书记员苏志伟</div></td></tr></tbody></table>";

        Document document = Jsoup.parse(html);
        Elements divs = document.select("div, p");
        StringBuilder s = new StringBuilder();
        for (Element div : divs) {
            String text = div.ownText();
            if (StringUtils.isNotBlank(text)) {
                if (text.contains("日") || text.contains("审") || text.contains("法") || text.contains("记")) {
                    s.append("\n").append(text);
                    continue;
                }
                s.append(text);
            }
        }
        System.out.println(s);
    }

    /**
     * 提取表格
     */
    @Test
    public void test20200509095215() {
        String html = "丁某、舒某等与怀化市鹤城区人民政府等交通运输行政管理（交通）：铁路行政管理（铁路）一审行政裁定书\n" +
            "文书内容\n" +
            "湖南省怀化市中级人民法院\n" +
            "行政裁定书\n" +
            "（2019）湘12行初93号\n" +
            "原告丁某，女，1982年4月18日出生，汉族，住湖南省怀化市鹤城区。\n" +
            "原告舒某，男，2009年2月4日出生，汉族，住湖南省怀化市鹤城区。\n" +
            "监护人丁某，系舒某之母。\n" +
            "以上二原告共同委托代理人舒小清，男，1979年9月21日出生，汉族，住湖南省中方县，系丁某老公、舒某父亲。\n" +
            "被告怀化市鹤城区人民政府，地址怀化市鹤城区金海路**号。\n" +
            "法定代表人向秀亮，区长。\n" +
            "委托代理人蒲俊均，男，怀化市鹤城区人民政府法制办公室公职律师。\n" +
            "被告鹤城区怀邵衡铁路项目协调服务指挥部。\n" +
            "负责人邓求君，指挥长。\n" +
            "委托代理人蒲俊均，男，怀化市鹤城区人民政府法制办公室公职律师。\n" +
            "本院在审理原告丁某、舒某诉被告怀化市鹤城区人民政府、鹤城区怀邵衡铁路项目协调服务指挥部征收补偿安置协议纠纷一案中，原告丁某、舒某以与被告协调解决本案纠纷为由，于2019年11月22日自愿向本院提出撤回起诉申请。\n" +
            "本院认为，原告丁某、舒某撤回本案起诉，系在法律规定的范围内处分自己的诉讼权利，没有损害国家、社会的利益以及他人的合法权益。依照《中华人民共和国行政诉讼法》第六十二条的规定，裁定如下：\n" +
            "准许原告丁某、舒某撤回起诉。\n" +
            "本案诉讼费用50元，减半收取25元，由原告丁某、舒某负担。\n" +
            "<table><tbody><tr><td width=\"291\"><div></div></td><td width=\"130\"><div>审判长</div></td><td width=\"35\"><div></div></td><td width=\"100\"><div>刘东利</div></td></tr><tr><td width=\"291\"><div></div></td><td width=\"130\"><div>审判员</div></td><td width=\"35\"><div></div></td><td width=\"100\"><div>王立志</div></td></tr><tr><td width=\"291\"><div></div></td><td width=\"130\"><div>审判员</div></td><td width=\"35\"><div></div></td><td width=\"100\"><div>李容容</div></td></tr><tr><td width=\"291\"><div></div></td><td colspan=\"3\"><div>二○一九年十一月二十二日</div></td></tr><tr><td width=\"291\"><div></div></td><td width=\"130\"><div>法官助理</div></td><td width=\"35\"><div></div></td><td width=\"100\"><div>刘俊卿</div></td></tr><tr><td width=\"291\"><div></div></td><td width=\"130\"><div>书记员</div></td><td width=\"35\"><div></div></td><td width=\"100\"><div>肖丽红</div></td></tr></tbody></table>";

        Matcher matcher = Pattern.compile("<table>(.*\n)*(.*)</table>").matcher(html);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }
}
