package demo.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2020/06/17 14:19
 */
public class JacksonDemo {

    /**
     * JsonNode类似于fastjson里的JSONPObject
     * ArrayNode类似fastjson里的JSONArray
     */
    @Test
    public void test20200617141921() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String s = "{\"took\":1751,\"timed_out\":false,\"_shards\":{\"total\":16,\"successful\":16,\"skipped\":0,\"failed\":0},\"hits\":{\"total\":33,\"max_score\":28.352083,\"hits\":[{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"662162100338011eb00c852dd65a799d\",\"_score\":28.352083,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"中华人民共和国公司法（1999修正）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"1368e4c7d7410df80b7a27f81986a938\",\"_score\":28.326885,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"中华人民共和国公司法（2004修正）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"aa5754c25f15f62c465fb548dc83a579\",\"_score\":28.326885,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"中华人民共和国公司法（2013修正）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"5405b7eb751b0f6ca699a9987db6d617\",\"_score\":28.325693,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"中华人民共和国公司法（1993）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"4224c333d199bb1da66cf364436adb28\",\"_score\":28.266722,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"中华人民共和国公司法（2018修订）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"4f12365d34b876bc1b89ae4b36add76\",\"_score\":28.232107,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"中华人民共和国公司法（2005修订）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"e3bc4996ba7d1978b30df944ce5909e3\",\"_score\":24.138132,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"关于《中华人民共和国公司法（修订草案）》的说明\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"7d18c56036653551840984db2aaaf3cd\",\"_score\":24.05926,\"_source\":{\"time_limited\":\"失效\",\"title\":\"辽宁省实施《中华人民共和国公司法》若干规定\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"a621e7f9d4c787d92f2c3f7695b4b30c\",\"_score\":22.203512,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"全国人大常委会关于修改《中华人民共和国公司法》的决定（2004）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"35fdfbea7bafbfdde263d0dfa9a63106\",\"_score\":22.161125,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"全国人大常委会关于修改《中华人民共和国公司法》的决定（1999）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"d81d5a5951c10f3739cd1cb75c165623\",\"_score\":20.982765,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（二）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"c5a23bb77b31e0e319d5c77f25719f27\",\"_score\":20.982765,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（三）（2014修订）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"4314112c85bfad757e7f8c7c51a79bf4\",\"_score\":20.934809,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（五）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"f237e5e58f2254df519b74128817bd7\",\"_score\":20.913055,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（一）（2014修正）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"61a18e01fbff4490c923de13745cfb58\",\"_score\":20.91145,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（二）（2014修正）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"4f4c29720f9f7e59c149932387118c5\",\"_score\":20.907078,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（三）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"c1b31eb5dd8becc4890d6764cf6a9f4c\",\"_score\":20.863516,\"_source\":{\"time_limited\":\"已被修改\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（一）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"375c9016aa0dc9fcda8c045318e97a1a\",\"_score\":20.863516,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于适用《中华人民共和国公司法》若干问题的规定（四）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"46959fa0bede501be9c9c21783aba65c\",\"_score\":20.293812,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"全国人民代表大会常务委员会关于修改《中华人民共和国公司法》的决定\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"35b9902add8edf5bab86a436c126ca70\",\"_score\":19.255634,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于修改关于适用《中华人民共和国公司法》若干问题的规定的决定（2014）\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"4315ffe3d8a33a87ca1d46f0daac8c38\",\"_score\":19.209314,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"辽宁省人大常委会关于废止《辽宁省实施〈中华人民共和国公司法〉若干规定》的决定\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"7824defbe7626ead72037cfda4dc73fb\",\"_score\":18.979496,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"最高人民法院关于认真学习《中华人民共和国公司法》、《中华人民共和国证券法》的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"169ed9b9dace75d0a2bc46f063b0b095\",\"_score\":18.031155,\"_source\":{\"time_limited\":\"失效\",\"title\":\"国务院关于原有有限责任公司和股份有限公司依照《中华人民共和国公司法》进行规范的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"38fad42e0e895140b52d89c7a45ca9f2\",\"_score\":17.786306,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"关于认真学习贯彻《全国人民代表大会常务委员会关于修改〈中华人民共和国公司法〉的决定》的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"f5b742ab8627ff756626397cc4b23704\",\"_score\":16.545208,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"四川省人民政府关于对原有有限责任公司和股份有限责任公司依照《中华人民共和国公司法》进行规范的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"d5e172f09f0752a9b9ab26109e97e01a\",\"_score\":16.182543,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"广东省人民政府转发国务院关于原有有限责任公司和股份有限公司依照《中华人民共和国公司法》进行规范的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"1562292f7d3365d339325cee1ae855f0\",\"_score\":16.17326,\"_source\":{\"time_limited\":\"征求意见稿或草案\",\"title\":\"关于就《最高人民法院关于适用〈中华人民共和国公司法〉若干问题的规定（四）》（征求意见稿）向社会公开征求意见的公告\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"cdebeba2c951aec5d8c2ea5b2cee4c29\",\"_score\":15.862717,\"_source\":{\"time_limited\":\"失效\",\"title\":\"北京市人民政府转发国务院关于原有有限责任公司和股份有限公司依照《中华人民共和国公司法》进行规范文件的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"c42749fed54f5ed8831e7729e2f512fd\",\"_score\":15.512084,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"中国人民银行关于贯彻《国务院关于原有有限责任公司和股份有限公司依照〈中华人民共和国公司法〉进行规范的通知》的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"994d551c9d59d859a439f2eb41878a45\",\"_score\":15.505882,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"青岛市人民政府关于贯彻《国务院关于原有有限责任公司和股份有限公司依照〈中华人民共和国公司法〉进行规范的通知》的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"8f1a4543486fc108abd86fbbb42cb35c\",\"_score\":15.486747,\"_source\":{\"time_limited\":\"失效\",\"title\":\"山东省人民政府关于贯彻国发［1995］17号文件对原有有限责任公司和股份有限公司依照《中华人民共和国公司法》进行规范的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"709c3d4fd2089ed5bb1e30f7dfd4e87d\",\"_score\":15.178836,\"_source\":{\"time_limited\":\"现行有效\",\"title\":\"广州市人民政府转发省人民政府转发国务院关于原有有限责任公司和股份有限公司依照《中华人民共和国公司法》进行规范的通知\"}},{\"_index\":\"law_200603\",\"_type\":\"law_regu\",\"_id\":\"f6e3b86f2164d4f20e5634d2926aa0cc\",\"_score\":14.836264,\"_source\":{\"time_limited\":\"失效\",\"title\":\"国家经济贸易委员会关于贯彻《国务院关于原有有限责任公司和股份有限公司依照〈中华人民共和国公司法〉进行规范的通知》有关问题的通知\"}}]}}";
        JsonNode jsonNode = mapper.readTree(s);
        ArrayNode arrayNode = (ArrayNode) jsonNode.get("hits").get("hits");

        for (JsonNode node : arrayNode) {
            JsonNode source = node.get("_source");
            System.out.println(source);

            // JsonNode转对象
            Map map = mapper.treeToValue(source, Map.class);
            System.out.println();
        }
        System.out.println();
    }

    /**
     * jsonStr转list
     */
    @Test
    public void test20200706164016() throws IOException {
        List<String> list = Lists.newArrayList("aaa", "bbb", "ccc", "ddd", "eee", "fff");

        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(list);

        List<String> list2 = mapper.readValue(s, List.class);
        System.out.println();
    }
}
