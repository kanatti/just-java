package com.kanatti.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageWrite {
    public static void main(String[] args) {
        try {
            Path indexPath = Paths.get("test-storage-index");
            Directory directory = FSDirectory.open(indexPath);
            
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            
            IndexWriter writer = new IndexWriter(directory, config);
            
            // --- Document 1 ---
            Document doc1 = new Document();
            // Inverted field: Indexed for search but not stored.
            doc1.add(new TextField("content", "Lucene is a powerful search library", Field.Store.NO));
            // Stored field: Title is stored and retrievable.
            doc1.add(new TextField("title", "Introduction to Lucene", Field.Store.YES));
            // Columnar (DocValues) numeric field for clicks.
            doc1.add(new NumericDocValuesField("clicks", 123));
            writer.addDocument(doc1);
            
            // --- Document 2 ---
            Document doc2 = new Document();
            doc2.add(new TextField("content", "Lucene supports indexing and search", Field.Store.NO));
            doc2.add(new TextField("title", "Advanced Lucene", Field.Store.YES));
            doc2.add(new NumericDocValuesField("clicks", 456));
            writer.addDocument(doc2);
            
            // --- Document 3 ---
            Document doc3 = new Document();
            doc3.add(new TextField("content", "Learn about DocValues in Lucene", Field.Store.NO));
            doc3.add(new TextField("title", "DocValues in Lucene", Field.Store.YES));
            doc3.add(new NumericDocValuesField("clicks", 789));
            writer.addDocument(doc3);
            
            writer.close();
            
            System.out.println("Index created successfully at: " + indexPath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
