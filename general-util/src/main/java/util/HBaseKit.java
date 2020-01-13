package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 该工具仅用作测试用，生产环境请不要使用
 * 待完善项：Connection、Table的关闭
 */
@Slf4j
public class HBaseKit {

    public enum DB {

        /**
         * 连接1
         */
        DB_1(
            "ds1:2181,ds2:2181,ds3:2181"
        );

        private Connection connection;

        DB(String quorum) {
            org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
            conf.set(HConstants.ZOOKEEPER_QUORUM, quorum);
            try {
                this.connection = ConnectionFactory.createConnection(conf);
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }

    public static Map<String, Object> findOne(String tableName, String rowKey) throws Exception {
        Map<String, Object> resultMap;
        try (Table table = DB.DB_1.connection.getTable(TableName.valueOf(tableName))) {
            if (table == null) {
                return null;
            }
            Get get = new Get(rowKey.getBytes());
            Result result = table.get(get);
            resultMap = new HashMap<>(10);
            for (Cell cell : result.rawCells()) {
                resultMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            log.error("", e);
            throw e;
        }
        return resultMap;
    }

    /**
     * 查询
     */
    @Test
    public void test20200106214811() throws Exception {
        final Map<String, Object> map = findOne("judgement_ds", "4bb5ce03ad03f1730c1de7f8bdc64d06");
        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat));
    }
}
