package com.lee.redis.config.cluster;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.lee.redis.exception.LocalException;

@Configuration
@PropertySource("redis/redis-cluster.properties")
public class RedisClusterConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConfig.class);
	
	// pool
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
	
	// cluster
	@Value("${redis.cluster.host}")
	private String host;
	@Value("${redis.cluster.port}")
	private String port;
	@Value("${redis.cluster.socketTimeout}")
	private int socketTimeout;
	@Value("${redis.cluster.connectionTimeOut}")
	private int connectionTimeOut;
	@Value("${redis.cluster.maxAttempts}")
	private int maxAttempts;
	@Value("${redis.cluster.maxRedirects}")
	private int maxRedirects;
	@Value("${redis.password}")
	private String password;
	
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
	
	@Bean
	public JedisCluster jedisCluster(JedisPoolConfig jedisPoolConfig) {
		
		if (StringUtils.isEmpty(host)) {
			LOGGER.info("redis集群主机未配置");
			throw new LocalException("redis集群主机未配置");
		}
		if (StringUtils.isEmpty(port)) {
			LOGGER.info("redis集群端口未配置");
			throw new LocalException("redis集群端口未配置");
		}
		String[] hosts = host.split(",");
		String[] portArray = port.split(";");
		if (hosts.length != portArray.length) {
			LOGGER.info("redis集群主机数与端口数不匹配");
			throw new LocalException("redis集群主机数与端口数不匹配");
		}
		Set<HostAndPort> redisNodes = new HashSet<HostAndPort>();
		for (int i = 0; i < hosts.length; i++) {
			String ports = portArray[i];
			String[] hostPorts = ports.split(",");
			for (String port : hostPorts) {
				HostAndPort node = new HostAndPort(hosts[i], Integer.parseInt(port));
				redisNodes.add(node);
			}
		}
		LOGGER.info("Set<RedisNode> : {}", JSON.toJSONString(redisNodes), true);
		
		return new JedisCluster(redisNodes, connectionTimeOut, socketTimeout, maxAttempts, password, jedisPoolConfig);
	}
}
