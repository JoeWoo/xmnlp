package org.xm.xmnlp.dictionary.stopword;

import org.xm.xmnlp.collection.MDAG.MDAGSet;
import org.xm.xmnlp.seg.domain.Term;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 停用词词典
 */
public class StopWordDictionary extends MDAGSet implements Filter {
    public StopWordDictionary(File file) throws IOException {
        super(file);
    }

    public StopWordDictionary(Collection<String> strCollection) {
        super(strCollection);
    }

    public StopWordDictionary() {
    }

    @Override
    public boolean shouldInclude(Term term) {
        return contains(term.word);
    }
}
