spring-boot基础上操作redis， 主要分以下几部分
	Springboot参考指南:https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/
	不管涉及到哪一部分，前提基础是对应的redis环境需要搭建好； 另外也需要注意redis、jedis的版本，版本不同满足的功能也不同
	redis-*.conf与sentinel-*.conf都是搭建环境是的初始配置，sentinel-*.conf在环境启动之后会有变化

1、redis sentinel连接与基本操作
	配置文件在sentinel文件夹下
	redis从节点作用：第一，当主节点出现故障时，从节点作为后备顶上来实现故障转移；第二，拓展主节点的读能力，尤其实在读多写少的场景
		从节点拓展主节点的读能力，并不是通过客户端直接连接从节点的方式实现，因为一旦从节点出现故障，那么该客户端将与该从节点失联，最终获取不到redis连接了
		与获取master连接一样，通过Redis Sentinel获取slave的连接，那么底层redis slave节点是否出现故障就不影响客户端了
		Redis Sentinel读写分离需要考虑具体场景，是否真的需要读写分离; redis是内存数据库，不存在关系型数据的IO性能瓶颈；
2、redis cluster连接与基本操作
	配置文件在cluster文件夹下；redis3.0版本正式推出redis cluster，解决redis分布式需求，当遇到单机内存、并发、流量等瓶颈时，可以采用Cluster架构方案达到负载均衡的目的
	redis-trib.rb工具可以帮我们简化集群创建、检查、槽迁移和均衡等常见操作
	集群模式下slaveof添加从节点操作不再支持；对集群的操作，像集群搭建、集群伸缩操作等都通过redis-trib.rb工具来进行；
	redis集群选主没有直接使用从节点进行领导者选举，避免从节点资源浪费；使用集群内主节点进行领导者选主，即使只有一个从节点也可以完成选举过程
	redis-trib.rb create --replicas 1 192.168.11.202:6382 192.168.11.202:6383 192.168.11.202:6384 192.168.11.202:6385 192.168.11.202:6386 192.168.11.202:6387
	redis-trib.rb fix 192.168.11.202:6388（若单独用此命令修复不了， 则重启集群再用此命令）
	redis-trib.rb add-node --slave --master-id e073db09e7aaed3c20d133726a26c8994932262c 192.168.11.202:6389 192.168.11.202:6382
	重新分槽：redis-trib.rb reshard 192.168.11.202:6388
	查看集群状态：redis-trib.rb check 192.168.11.202:6388
	redis-trib.rb del-node 192.168.11.202:6388 e073db09e7aaed3c20d133726a26c8994932262c，安全下线主节点，下线之前要保证下线节点的数据和槽都完成了迁移
3、redis做缓存、实现session共享
	