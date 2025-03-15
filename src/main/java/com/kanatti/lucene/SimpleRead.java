package com.kanatti.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRead {
    private static final Logger log = LoggerFactory.getLogger(SimpleRead.class);

    public static void main(String[] args) {
        try {
            Path indexPath = Paths.get("test-simple-index");
            Directory index = FSDirectory.open(indexPath);

            IndexReader reader = DirectoryReader.open(index);

            log.info("Doc count: {}, Segments: {}", reader.maxDoc(), reader.leaves().size());

            for (LeafReaderContext leaf : reader.leaves()) {
                LeafReader leafReader = leaf.reader();
                Bits liveDocs = leafReader.getLiveDocs();
                int docBase = leaf.docBase;
                for (int i = 0; i < leafReader.maxDoc(); i++) {
                    if (liveDocs != null && !liveDocs.get(i)) {
                        continue;
                    }
                    Document doc = leafReader.document(i);

                    log.info("---");
                    log.info("Document ID: {}", (docBase + i));
                    for (IndexableField field : doc.getFields()) {
                        log.info("{}: {}", field.name(), field.stringValue());
                    }
                    log.info("---");
                }
            }
            
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
