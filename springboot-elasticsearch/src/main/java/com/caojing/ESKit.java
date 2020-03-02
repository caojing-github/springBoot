package com.caojing;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Objects;
import java.util.Properties;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * ES工具
 */
@Slf4j
public class ESKit {

    public enum ES {

        /**
         * 线上ES环境
         */
        PRO("judgementsearch", "172.16.76.101:9705,172.16.76.102:9705,172.16.76.103:9705,172.16.76.104:9705,172.16.76.105:9705");

        private ElasticsearchTemplate elasticsearchTemplate;

        ES(String clusterName, String clusterNodes) {
            try {
                ElasticsearchProperties esProperties = new ElasticsearchProperties();
                esProperties.setClusterName(clusterName);
                esProperties.setClusterNodes(clusterNodes);

                Properties properties = new Properties();
                properties.put("cluster.name", esProperties.getClusterName());
                properties.putAll(esProperties.getProperties());

                TransportClientFactoryBean factory = new TransportClientFactoryBean();
                factory.setClusterNodes(esProperties.getClusterNodes());
                factory.setProperties(properties);
                factory.afterPropertiesSet();

                this.elasticsearchTemplate = new ElasticsearchTemplate(Objects.requireNonNull(factory.getObject()));
            } catch (Exception e) {
                log.error("ES初始化异常", e);
            }
        }
    }

    /**
     * 通过jid查询
     */
    @Test
    public void test20200302114543() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withIndices("judgementsearch_ds")
            .withTypes("judgement")
            .withQuery(termQuery("jid", "7820BAE443D286A2D090590EA7905520"))
            .build();

        JSONObject jsonObject = ES.PRO.elasticsearchTemplate.queryForList(searchQuery, JSONObject.class).get(0);
        System.out.println(jsonObject.toJSONString());
    }
}
