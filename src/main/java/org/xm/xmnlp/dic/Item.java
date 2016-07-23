package org.xm.xmnlp.dic;

/**
 * Created by xuming on 2016/7/6.
 */
public class Item {
    public String word;

    public int startOffset;

    public int endOffset;

    public String nature;//add by xuming 20160607

    public Item(String word, int startOffset, int endOffset) {
        this.word = word;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public Item(String word, int startOffset, int endOffset,String nature) {
        this.word = word;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.nature = nature;
    }

//    @Override
//    public String toString() {
//        return "[" + word + ", " + startOffset + ", " + endOffset + "]";
//    }

    @Override
    public String toString() {
        return word + "/" + nature;
    }

}
