package com.kanatti.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleWrite {
    public static void main(String[] args) {
        try {
            Path indexPath = Paths.get("test-simple-index");
            
            Directory index = FSDirectory.open(indexPath);

            StandardAnalyzer analyzer = new StandardAnalyzer();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(index, config);

            // Create a document and add fields to it
            Document doc = new Document();
            doc.add(new TextField("title", "Hello World", Field.Store.YES));
            doc.add(new TextField("content", "This is a test document.", Field.Store.NO));

            // Add the document to the index
            writer.addDocument(doc);

            // Commit changes and close the writer
            writer.close();

            System.out.println("Index created successfully in: " + indexPath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
