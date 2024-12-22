### 1.基于sts + hpa的方式部署pod
保证单个pod的mem、cpu使用率超过阈值时，k8s会自动扩缩容

### 2.kubectl delete pod，k8s自动拉起
```markdown
(base) hanzhengting@COLINHAN-MB1 hsbc % kubectl get pods           
NAME           READY   STATUS    RESTARTS       AGE
hsbc-trade-0   1/1     Running   0              76s
hsbc-trade-1   1/1     Running   0              75s
hsbc-trade-2   1/1     Running   0              74s
mysql-0        1/1     Running   3 (5h8m ago)   5d20h
redis-0        1/1     Running   1 (5h8m ago)   23h
(base) hanzhengting@COLINHAN-MB1 hsbc % kubectl delete pod  hsbc-trade-2
pod "hsbc-trade-2" deleted
(base) hanzhengting@COLINHAN-MB1 hsbc % kubectl get pods                
NAME           READY   STATUS    RESTARTS       AGE
hsbc-trade-0   1/1     Running   0              85s
hsbc-trade-1   1/1     Running   0              84s
hsbc-trade-2   1/1     Running   0              2s
mysql-0        1/1     Running   3 (5h8m ago)   5d20h
redis-0        1/1     Running   1 (5h8m ago)   23h
```
![img.png](../png/resilience-1.png)

### 3.kubectl scala扩容
```markdown
kubectl scale sts hsbc-trade --replicas=5
```