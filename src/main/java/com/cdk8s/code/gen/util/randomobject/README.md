
# 用于自动填充对象里面的属性

```
int total = 10;
List<WeiboStatus> list = new ArrayList<>(total);

for (int i = 0; i < total; i++) {
	list.add(RandomObjectValue.getObject(WeiboStatus.class));
}
```
