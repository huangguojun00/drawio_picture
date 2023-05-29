# C++学习

## 定义数据结构

### typedef

```c++
  typedef struct BiTNode{
      struct BiTNode* l,r;
             int weight;
  }BiTNode,*BiTree;
```

上面表示了定义一个数据接口类型是BiTNode,别名为BiTNode,以后再使用的时候就可以不加struct了
