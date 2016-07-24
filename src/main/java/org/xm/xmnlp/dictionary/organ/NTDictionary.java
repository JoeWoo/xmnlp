package org.xm.xmnlp.dictionary.organ;


import org.xm.xmnlp.corpus.dictionary.item.EnumItem;
import org.xm.xmnlp.corpus.tag.NT;
import org.xm.xmnlp.dictionary.common.CommonDictionary;
import org.xm.xmnlp.util.ByteUtil;
import org.xm.xmnlp.util.IOUtil;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.xm.xmnlp.util.Predefine.logger;

/**
 * 一个好用的地名词典
 *
 */
public class NTDictionary extends CommonDictionary<EnumItem<NT>> {
    @Override
    protected EnumItem<NT>[] onLoadValue(String path) {
        EnumItem<NT>[] valueArray = loadDat(path + ".value.dat");
        if (valueArray != null) {
            return valueArray;
        }
        List<EnumItem<NT>> valueList = new LinkedList<EnumItem<NT>>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                Map.Entry<String, Map.Entry<String, Integer>[]> args = EnumItem.create(line);
                EnumItem<NT> NSEnumItem = new EnumItem<NT>();
                for (Map.Entry<String, Integer> e : args.getValue()) {
                    NSEnumItem.labelMap.put(NT.valueOf(e.getKey()), e.getValue());
                }
                valueList.add(NSEnumItem);
            }
            br.close();
        } catch (Exception e) {
            logger.warning("读取" + path + "失败" + e);
        }
        valueArray = valueList.toArray(new EnumItem[0]);
        return valueArray;
    }

    @Override
    protected boolean onSaveValue(EnumItem<NT>[] valueArray, String path) {
        return saveDat(path + ".value.dat", valueArray);
    }

    private EnumItem<NT>[] loadDat(String path) {
        byte[] bytes = IOUtil.readBytes(path);
        if (bytes == null) return null;
        NT[] values = NT.values();
        int index = 0;
        int size = ByteUtil.bytesHighFirstToInt(bytes, index);
        index += 4;
        EnumItem<NT>[] valueArray = new EnumItem[size];
        for (int i = 0; i < size; ++i) {
            int currentSize = ByteUtil.bytesHighFirstToInt(bytes, index);
            index += 4;
            EnumItem<NT> item = new EnumItem<NT>();
            for (int j = 0; j < currentSize; ++j) {
                NT tag = values[ByteUtil.bytesHighFirstToInt(bytes, index)];
                index += 4;
                int frequency = ByteUtil.bytesHighFirstToInt(bytes, index);
                index += 4;
                item.labelMap.put(tag, frequency);
            }
            valueArray[i] = item;
        }
        return valueArray;
    }

    private boolean saveDat(String path, EnumItem<NT>[] valueArray) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(path));
            out.writeInt(valueArray.length);
            for (EnumItem<NT> item : valueArray) {
                out.writeInt(item.labelMap.size());
                for (Map.Entry<NT, Integer> entry : item.labelMap.entrySet()) {
                    out.writeInt(entry.getKey().ordinal());
                    out.writeInt(entry.getValue());
                }
            }
            out.close();
        } catch (Exception e) {
            logger.warning("保存失败" + e);
            return false;
        }
        return true;
    }
}
