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
 * HBase工具
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

    /**
     * 通过rowKey查询
     */
    public static Map<String, Object> findOne(String tableName, String rowKey) throws IOException {
        Map<String, Object> resultMap;
        try (Table table = DB.DB_1.connection.getTable(TableName.valueOf(tableName))) {
            if (table == null) {
                return null;
            }
            Get get = new Get(rowKey.getBytes());
            Result result = table.get(get);
            resultMap = new HashMap<>(16);
            for (Cell cell : result.rawCells()) {
                resultMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        return resultMap;
    }

    /**
     * 根据rowKey删除指定的行
     */
    public static void deleteRow(String tableName, String rowKey) throws IOException {
        try (Table table = DB.DB_1.connection.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        }
    }

    /**
     * 查询
     */
    @Test
    public void test20200106214811() throws Exception {
        final Map<String, Object> map = findOne("judgement_ds", "35C0D1C8A8729AAC804A055C74E25055");
        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat));
    }

    /**
     * 删除
     */
    @Test
    public void test20200211143947() throws IOException {
        // 删除
        deleteRow("judgement_ds", "DA78EDAC724CE10D35CD9F7303082FF4");
        // 查询
        final Map<String, Object> map = findOne("judgement_ds", "912BEBCF6B6103243BCE2D3945B13AC3");
        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat));
    }
}
