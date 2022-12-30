package com.sk.elasticsearch.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Service;

import com.sk.elasticserach.helper.Indices;

@Service
public class IndexService {
	private static final List<String> INDICES = List.of(Indices.DUMMY_INDEX, Indices.USER_INDEX);

	RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

	public boolean isIndexAvailableOrNot(String index) {

		try {
			final boolean indexExists = client.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
			if (indexExists) {
				return true;
			} else {
				String s = index(index);
			}
		} catch (final Exception e) {
		}

		return false;
	}

	public String index(String indexName) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(indexName);
		request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 2));
		CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		String index1 = createIndexResponse.index();
		System.out.println("response id: " + createIndexResponse.index());
		return index1;
	}

}
