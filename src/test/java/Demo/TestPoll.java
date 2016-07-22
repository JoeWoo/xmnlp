package Demo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by xuming on 2016/7/8.
 */
public class TestPoll {
    public static void main(String[] args) {
        Queue<String> queue=new LinkedList<String>();

        queue.offer("Hello");
        queue.offer("world!");
        queue.offer("您好！");

        System.out.println("使用offer()之后队列的长度："+queue.size());
        System.out.println("使用peek()取出第一个元素：  "+queue.peek());
        System.out.println("使用element()取出第一个元素：  "+queue.element());
        System.out.println("使用peek/element之后队列的长度：  "+queue.size());
        System.out.println("使用poll()循环取出并删除元素："+queue.poll());
        String str;
        while((str=queue.poll())!=null)
        {
            System.out.print(" "+str);
        }
        System.out.println();
        System.out.print("使用poll()后队列的长度："+queue.size());
    }
}
