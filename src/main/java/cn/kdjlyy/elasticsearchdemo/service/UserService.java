package cn.kdjlyy.elasticsearchdemo.service;


import cn.kdjlyy.elasticsearchdemo.constant.Constant;
import cn.kdjlyy.elasticsearchdemo.document.UserDocument;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    @Autowired
    private RestHighLevelClient client;

    public boolean createUserIndex(String index) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );
        createIndexRequest.mapping("_doc", "{\n" +
                "  \"properties\": {\n" +
                "    \"city\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"sex\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"name\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"id\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\": \"integer\"\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    // 批量新增文档
    public Boolean bulkCreateUserDocument(List<UserDocument> documents) throws IOException {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            // 创建index请求 千万注意，这个写在循环外侧，否则UDP协议会有丢数据的情况，看运气
            IndexRequest indexRequest = null;
            for (UserDocument document : documents) {
                String id = UUID.randomUUID().toString();
                document.setId(id);

                indexRequest = new IndexRequest(Constant.INDEX, "_doc")
                        .id(id)
                        .source(JSON.toJSONString(document), XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            log.info("es同步数据数量:{}", bulkRequest.numberOfActions());

            // 设置索引刷新规则
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            BulkResponse bulkResponse = null;
            // 分批次提交，数量控制
            if (bulkRequest.numberOfActions() >= 1) {
                bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                log.info("es同步数据结果:{}", bulkResponse.hasFailures());
            }
            if (bulkResponse != null) {
                return bulkResponse.status().equals(RestStatus.CREATED);
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("es同步数据执行失败:{}", documents);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
