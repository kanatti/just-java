package com.kanatti.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageRead {
    private static final Logger log = LoggerFactory.getLogger(StorageRead.class);

    public static void main(String[] args) {
        try {
            Path indexPath = Paths.get("test-storage-index");
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
                    log.info("Global Document ID: " + globalDocId);

                    doc.getFields().forEach(
                            field -> log.info("Field: " + field.name() + " => " + field.stringValue()));

                    log.info("---------------------------");
                }

                log.info("---------------------------");

                // Retrieve NumericDocValues for the "clicks" field
                NumericDocValues clicksValues = leafReader.getNumericDocValues("clicks");

                for (int i = 0; i < leafReader.maxDoc(); i++) {
                    // Retrieve the numeric DocValues field "clicks"
                    if (clicksValues != null && clicksValues.advanceExact(i)) {
                        long clicks = clicksValues.longValue();
                        log.info("DocValues Field 'clicks': " + clicks);
                    } else {
                        log.info("DocValues Field 'clicks' not available");
                    }
                }
            }

            IndexSearcher searcher = new IndexSearcher(reader);

            log.info("---------------------------");
            log.info("Searching content:library");
            Query query = new TermQuery(new Term("content", "library"));
            TopDocs topDocs = searcher.search(query, 10);
            
            log.info("Total hits: {}", topDocs.totalHits);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                Document resultDoc = searcher.doc(docId);
                log.info("Doc ID: {}", docId);
                log.info("Title: {}", resultDoc.get("title"));
                log.info("Content: {}", resultDoc.get("content"));
                log.info("---------------------------");
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
