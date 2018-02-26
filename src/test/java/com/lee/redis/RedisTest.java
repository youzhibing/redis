package com.lee.redis;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import com.alibaba.fastjson.JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
	
	@Value("${redis.masterName}")
	private String masterName;
	
	@Autowired
	private JedisSentinelPool sentinelPool;
	
	@Test
	public void getNameTest() {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
			String name = jedis.get("name");
			System.out.println("name is " + name);
			
			// 可以通过slave节点信息，来获取其中某个slave节点的连接，并从次slave读取数据，达到读写分离
			/*List<Map<String, String>> slaves = jedis.sentinelSlaves(masterName);	// Jedis实现了SentinelCommands接口； ERR unknown command 'SENTINEL'错误
			System.out.println("slaves :  " + JSON.toJSONString(slaves, true));*/
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
				sentinelPool.close();
			}
		}
		
	}
	
}
