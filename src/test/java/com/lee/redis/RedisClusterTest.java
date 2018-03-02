package com.lee.redis;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationCluster.class)
public class RedisClusterTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterTest.class);
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Test
	public void initTest() {
		String name = jedisCluster.get("name");
		LOGGER.info("name is {}", name);
		
		// list操作
		long count = jedisCluster.lpush("list:names", "陈芸");	// lpush的返回值是在 push操作后的 list 长度
		LOGGER.info("count = {}", count);
		long nameLen = jedisCluster.llen("list:names");
		LOGGER.info("list:names lens is {}", nameLen);
		List<String> nameList = jedisCluster.lrange("list:names", 0, nameLen);
		LOGGER.info("names : {}", JSON.toJSONString(nameList));
		
		/*Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		int index = 1;
		for (String key : clusterNodes.keySet()) {
			JedisPool pool = clusterNodes.get(key);
			System.out.print(pool.getNumActive() + " ");
			System.out.println(index++);
		}*/
	}
}
