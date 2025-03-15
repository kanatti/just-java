package com.kanatti.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESRead {
    private static final Logger log = LoggerFactory.getLogger(ESRead.class);

    public static void main(String[] args) {
        try {
            Path indexPath = Paths.get("test-es-index");
            Directory directory = FSDirectory.open(indexPath);

            DirectoryReader reader = DirectoryReader.open(directory);
            log.info("Documents: {}, Segments: {}", reader.maxDoc(), reader.leaves().size());

            // Iterate over each segment (leaf) in the index
            for (LeafReaderContext leaf : reader.leaves()) {
                LeafReader leafReader = leaf.reader();
                int docBase = leaf.docBase;

                log.info("---------------------------");

                // Iterate over documents in the current segment
                for (int i = 0; i < leafReader.maxDoc(); i++) {
                    int globalDocId = docBase + i;
                    Document doc = leafReader.document(i);
                    log.info("Global Document ID: {}" , globalDocId);

                    doc.getFields().forEach(
                            field -> log.info("Field: {} => {}", field.name(), getValue(field)));

                    log.info("---------------------------");
                }

            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(IndexableField field) {
        switch (field.name()) {
            case "_id": {
                return Uid.decodeId(field.binaryValue().bytes);
            }
            case "_source": {
                return field.binaryValue().utf8ToString();
            }
            case "_routing": {
                return field.stringValue();
            }
            default:
                return "NO_MATCH";
        }
    }
}
