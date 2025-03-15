# Kanatti Notes

## Run ES + OS on local

```sh
docker-compose -f docker/es-os-compose.yml up
```


## Run RFS on local

```sh
sdk use java 11.0.23-amzn

./gradlew :DocumentsFromSnapshotMigration:tasks

./gradlew DocumentsFromSnapshotMigration:run  --args="--help"

SNAPSHOT_DIR=/home/kanatti/Code/opensearch-migrations/DocumentsFromSnapshotMigration/docker/snapshots
SNAPSHOT_ARGS="--snapshot-name snapshot_2 --snapshot-local-dir $SNAPSHOT_DIR"
./gradlew DocumentsFromSnapshotMigration:run --args="$SNAPSHOT_ARGS --lucene-dir /tmp/lucene_files --target-host http://localhost:29200 --source-version 'ES 6.8'" 


./gradlew DocumentsFromSnapshotMigration:runDebug --args="$SNAPSHOT_ARGS --lucene-dir /tmp/lucene_files --target-host http://localhost:29200 --source-version 'ES 6.8' --initial-lease-duration PT60M" 


rm -rf /tmp/lucene_files

chmod -R 777 ./snapshots

sudo rm -rf /home/kanatti/Code/opensearch-migrations/DocumentsFromSnapshotMigration/docker/snapshots/*


```



## Tests

```sh
./gradlew :DocumentsFromSnapshotMigration:isolatedTest --tests "*EndToEndTest.*"

./gradlew :RFS:test --tests "*BulkDocSectionTest.*"

./gradlew :RFS:test --tests "*DocumentReindexerTest.*"

./gradlew :RFS:test --tests "*LuceneDocumentsReaderTest.*"

./gradlew :RFS:test --tests "*RestClientTest.*"


./gradlew :RFS:isolatedTest --tests "*LuceneDocumentsReaderTest.*"
```


## Inspect

```sh
./gradlew DocumentsFromSnapshotMigration:run  --args="--help"
```


## TODO


- Update Unit testing
    - Snapshots and inventory
    - LuceneDocumentsReader
    - DocumentReIndexer
        - Transformers?
- Create PR
- Deep dive into abstractions of interest

