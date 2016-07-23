package org.xm.xmnlp.seg.domain;


/**
 * 记录了起点的边
 * Created by mingzai on 2016/7/23.
 */
public class EdgeFrom extends Edge {
    public int from;

    public EdgeFrom(int from, double weight, String name) {
        super(weight, name);
        this.from = from;
    }

    @Override
    public String toString() {
        return "EdgeFrom{" +
                "from=" + from +
                ", weight=" + weight +
                ", name='" + name + '\'' +
                '}';
    }
}
