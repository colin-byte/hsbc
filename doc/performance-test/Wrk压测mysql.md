# 说明：
这份压测报告是基于`mysql`实现的金融转账业务，
详细逻辑见com.hsbc.trade.domain.service.impl.TransactionDomainServiceImpl#modify(TransactionEntity entity)
方法

### 1.集群配置
详见Wrk压测lua文件相关章节

### 2.压测性能
设置参数-c=2,-t=2，最终压测结果为：qps=93,P99=177ms
```markdown
1.使用wrk进行性能测试
命令：
wrk -c 2 -t 2 -d 10 --script=/Users/hanzhengting/opt/wrk/post.lua http://127.0.0.1:8081/transaction/process --latency
-c:保持连接数
-t:线程数
-d:持续时间(单位：s）
--latency: 打印延迟统计

2.性能测试如下：
性能最佳测试的参数如下：
wrk -c 10 -t 2 -d 10 --script=/Users/hanzhengting/opt/wrk/post.lua http://127.0.0.1:8080/transaction/process --latency
设置2个线程，10个链接的参数时，qps达到了93，平均耗时再93ms，具体性能指标如下：

  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    55.48ms   21.57ms 248.38ms   94.78%
    Req/Sec    93.13     21.34   121.00     78.89%
  Latency Distribution
     50%   48.84ms
     75%   57.79ms
     90%   66.29ms
     99%  177.76ms
  1859 requests in 10.06s, 624.69KB read
Requests/sec:    184.70
Transfer/sec:     62.07KB
```
![压测结果mysql版.png](%E5%8E%8B%E6%B5%8B%E7%BB%93%E6%9E%9Cmysql%E7%89%88.png)