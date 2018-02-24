package com.lee.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTest.class);
	
	@Autowired
	private JedisSentinelPool sentinelPool;
	
	@Test
	public void getNameTest() {
		Jedis jedis = null;
		try {
			jedis = sentinelPool.getResource();
			String name = jedis.get("name");
			System.out.println("name is " + name);
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
	}
	
}
