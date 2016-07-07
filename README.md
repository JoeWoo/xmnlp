# 中文分词工具


##### 2016.05.26 start in shenzhen
##### author：xuming

---
environment：jdk 1.7 

1. build the project by IDEA 2016.1.
2. add the rule chinese word segmentation: 2016.06.21
    - 正向最大匹配法
    - 逆向最大匹配法
    - 双向最大匹配法
3. add jieba segmentation java:2016.07.06

* 简介
** 支持分词模式
   - Search模式，用于对用户查询词分词
   - Index模式，用于对索引文档分词

** 特性
   - 支持多种分词模式
   - 全角统一转成半角
   - 用户词典功能
   - conf 目录有整理的搜狗细胞词库

* 如何获取
  - 当前稳定版本
    #+BEGIN_SRC xml
      <dependency>
          <groupId>com.pycredit.nlpfirst</groupId>
          <artifactId>segmentation</artifactId>
          <version>1.0-SNAPSHOT</version>
      </dependency>
      

    #+END_SRC


* 如何使用
  - Demo
  #+BEGIN_SRC java
    Segmenter segmenter = new Segmenter();
            String[] sentences =
                    new String[] {"不輸入簡體字,阿丁说你很好，黎明认识这个李明不輸入簡體字典,矿泉水瓶盖子下面有东西", "我不喜欢日本和服。", "雷猴回归人间。",
                            "李明不輸入簡體字", "我不喜欢日本和服。", "东海县迅捷贸易有限责任公司"};
            System.out.println("----------------------");
            for (String sentence : sentences) {
                System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
            }
  #+END_SRC

* 算法(wiki补充...)
  - [ ] 基于 =trie= 树结构实现高效词图扫描
  - [ ] 生成所有切词可能的有向无环图 =DAG=
  - [ ] 采用动态规划算法计算最佳切词组合
  - [ ] 基于 =HMM= 模型，采用 =Viterbi= (维特比)算法实现未登录词识别

* 性能评估
  - 测试机配置
  #+BEGIN_SRC screen
    Processor 2 Intel(R) Pentium(R) CPU G620 @ 2.60GHz
    Memory：8GB

    分词测试时机器开了许多应用(eclipse、emacs、chrome...)，可能
    会影响到测试速度
  #+END_SRC
  - 测试结果(单线程，对测试文本逐行分词，并循环调用上万次)
    #+BEGIN_SRC screen
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

    #+END_SRC

* 许可证
  jieba(python版本)的许可证为MIT，jieba(java版本)的许可证为ApacheLicence 2.0
  #+BEGIN_SRC screen
    Copyright (C) 2013 Huaban Inc

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
  #+END_SRC
    

