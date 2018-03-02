package com.lee.redis.config.sentinel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

@Configuration
@PropertySource("redis/redis-sentinel.properties")
public class RedisConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
	
	@Value("${redis.masterName}")
	private String masterName;
	@Value("${redis.sentinels}")
	private String sentinels;
	@Value("${redis.timeout}")
	private int timeout;
	@Value("${redis.password}")
	private String password;
	
	@Value("${redis.pool.maxActive}")
	private int maxTotal;

	@Value("${redis.pool.maxIdle}")
	private int maxIdle;

	@Value("${redis.pool.minIdle}")
	private int minIdle;

	@Value("${redis.pool.maxWaitMillis}")
	private long maxWaitMillis;

	@Value("${redis.pool.numTestsPerEvictionRun}")
	private int numTestsPerEvictionRun;

	@Value("${redis.pool.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;

	@Value("${redis.pool.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;

	@Value("${redis.pool.softMinEvictableIdleTimeMillis}")
	private long softMinEvictableIdleTimeMillis;

	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${redis.pool.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${redis.pool.testOnReturn}")
	private boolean testOnReturn;

	@Value("${redis.pool.blockWhenExhausted}")
	private boolean blockWhenExhausted;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public JedisSentinelPool jedisSentinelPool(JedisPoolConfig poolConfig) {
		LOGGER.info("sentinel set : {}", sentinels);
		Set sentinelSet = new HashSet(Arrays.asList(sentinels.split(",")));
		JedisSentinelPool jedisPool = new JedisSentinelPool(masterName, sentinelSet, poolConfig, timeout, password);
		return jedisPool;
	}
	
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		jedisPoolConfig
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		jedisPoolConfig
				.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		jedisPoolConfig
				.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);

		return jedisPoolConfig;
	}
}
