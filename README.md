# xmnlp自然语言处理包


#### author：xuming(shibing624) 
#### environment：jdk 1.6
    

1. add the rule chinese word segmentation: 2016.06.21
    - 正向最大匹配法
    - 逆向最大匹配法
    - 双向最大匹配法
2. add jieba segmentation java:2016.07.06
3. add xmnlp to xmnlp 2016.07.23
	- 最短路径分词，最短路求解采用Viterbi算法
	- 添加人名识别功能：中文人名，日本人名识别



---

* 简介
**xmnlp**是由一系列模型与算法组成的Java工具包，目标是普及自然语言处理在生产环境中的应用。**xmnlp**具备功能完善、性能高效、架构清晰、语料时新、可自定义的特点。

**xmnlp**提供下列功能：

> * 中文分词
  * 最短路分词
  * N-最短路分词
  * CRF分词
  * 索引分词
  * 极速词典分词
  * 用户自定义词典
> * 词性标注
> * 命名实体识别
  * 中国人名识别
  * 音译人名识别
  * 日本人名识别
  * 地名识别
  * 实体机构名识别
> * 关键词提取
  * TextRank关键词提取
> * 自动摘要
  * TextRank自动摘要
> * 短语提取
  * 基于互信息和左右信息熵的短语提取
> * 拼音转换
  * 多音字
  * 声母
  * 韵母
  * 声调
> * 简繁转换
  * 繁体中文分词
  * 简繁分歧词
> * 文本推荐
  * 语义推荐
  * 拼音推荐
  * 字词推荐
> * 依存句法分析
  * 基于神经网络的高性能依存句法分析器
  * MaxEnt依存句法分析
  * CRF依存句法分析
> * 语料库工具
  * 分词语料预处理
  * 词频词性词典制作
  * BiGram统计
  * 词共现统计
  * CoNLL语料预处理


在提供丰富功能的同时，**xmnlp**内部模块坚持低耦合、模型坚持惰性加载、服务坚持静态提供、词典坚持明文发布，使用非常方便，同时自带一些语料处理工具，帮助用户训练自己的语料。

------

#### 支持中文分词模式
   - Search模式，用于对用户查询词分词
   - Index模式，用于对索引文档分词

#### 特性
   - 支持多种分词模式
   - 全角统一转成半角
   - 用户词典功能
   - conf 目录有整理的搜狗细胞词库和一个自定义词库，可加载多个用户词库

#### 如何获取
  - 当前稳定版本
  
    ```
  	<dependency>
            <groupId>com.xm</groupId>
            <artifactId>xmnlp</artifactId>
            <version>1.0.1</version>
    </dependency>
  	```
    
## 调用方法

**xmnlp**几乎所有的功能都可以通过工具类`Xmnlp`快捷调用，当你想不起来调用方法时，只需键入`Xmnlp.`，IDE应当会给出提示，并展示**xmnlp**完善的文档。

*推荐用户始终通过工具类`Xmnlp`调用，这么做的好处是，将来**xmnlp**升级后，用户无需修改调用代码。*

所有Demo都位于[org.xm.xmnlp.demo](https://github.com/shibing624/xmnlp/tree/master/src/test/java/org/xm/xmnlp/demo)下，比文档覆盖了更多细节，强烈建议运行一遍。

#### 如何使用

  - Demo
	
	
	```
	System.out.println(Xmnlp.segment("你好，欢迎使用xmnlp自然语言处理包！"));
	```


#### 算法
  - [ ] 基于 =trie= 树结构实现高效词图扫描
  - [ ] 生成所有切词可能的有向无环图 =DAG=
  - [ ] 采用动态规划算法计算最佳切词组合
  - [ ] 基于 =HMM= 模型，采用 =Viterbi= (维特比)算法实现未登录词识别

#### 性能评估
  - 测试机配置
	```
	Processor 2 Intel(R) Pentium(R) CPU G620 @ 2.60GHz
	Memory：8GB
	
	分词测试时机器开了许多应用(eclipse、emacs、chrome...)，可能
	会影响到测试速度
	```
  
  - 测试结果(单线程，对测试文本逐行分词，并循环调用上万次)
	```
	  循环调用一万次
	  第一次测试结果：
	  time elapsed:12373, rate:2486.986533kb/s, words:917319.94/s
	  第二次测试结果：
	  time elapsed:12284, rate:2505.005241kb/s, words:923966.10/s
	  第三次测试结果：
	  time elapsed:12336, rate:2494.445880kb/s, words:920071.30/s
	
	  循环调用2万次
	  第一次测试结果：
	  time elapsed:22237, rate:2767.593144kb/s, words:1020821.12/s
	  第二次测试结果：
	  time elapsed:22435, rate:2743.167762kb/s, words:1011811.87/s
	  第三次测试结果：
	  time elapsed:22102, rate:2784.497726kb/s, words:1027056.34/s
	  统计结果:词典加载时间1.8s左右，分词效率每秒2Mb多，近100万词。
	
	  2 Processor Intel(R) Core(TM) i3-2100 CPU @ 3.10GHz
	  12G 测试效果
	  time elapsed:19597, rate:3140.428063kb/s, words:1158340.52/s
	  time elapsed:20122, rate:3058.491639kb/s, words:1128118.44/s
	```

    

#### 许可证
  许可证为ApacheLicence 2.0
  
  
        Copyright (C) 2016 xm Inc
        
        Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
        
        http://www.apache.org/licenses/LICENSE-2.0
        
        Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
     
    

