package com.kanatti.routing;

public class RoutingTest {
    private static int NUM_PRIMARY_SHARDS = 5;
    private static int ROUTING_PARTITION_SIZE = 4;
    private static int[] SHARD_HASHES = {100, 101, 102, 103, 104, 105, 106, 107, 108, 109};

  public static void main(String args[]) {
    System.out.println("shards " + calculateNumRoutingShards(10));
    System.out.println("shards " + calculateNumRoutingShards(200));
    System.out.println("shards " + calculateNumRoutingShards(840));
    System.out.println("shards " + calculateNumRoutingShards(1000));
    
    System.out.println("routing-factor 1");

    for (int shardHash : SHARD_HASHES) {
        System.out.println("floor mod " + getShardNum(shardHash, NUM_PRIMARY_SHARDS, 1));
    }
    
    System.out.println("routing-factor 2");
    for (int shardHash : SHARD_HASHES) {
        System.out.println("floor mod " + getShardNum(shardHash, NUM_PRIMARY_SHARDS*2, 2));
    }

    System.out.println("routing-factor 1, routing_parition 4");

    for (int shardHash : SHARD_HASHES) {
        System.out.println("floor mod " + getShardNumWithPartition(shardHash, NUM_PRIMARY_SHARDS, 1));
    }
    
    System.out.println("routing-factor 2, routing_parition 4");
    for (int shardHash : SHARD_HASHES) {
        System.out.println("floor mod " + getShardNumWithPartition(shardHash, NUM_PRIMARY_SHARDS*2, 2));
    }

  }
  
  public static int calculateNumRoutingShards(int numShards) {
    int log2MaxNumShards = 10;
    int log2NumShards = 32 - Integer.numberOfLeadingZeros(numShards - 1);
    int numSplits = log2MaxNumShards - log2NumShards;
    numSplits = Math.max(1, numSplits); 
    return numShards * 1 << numSplits;
  }
  
  public static int getShardNum(int shardHash, int routingNumShards, int routingFactor) {
       return Math.floorMod(shardHash, routingNumShards) / routingFactor;
  }
  
  public static int getShardNumWithPartition(int shardHash, int routingNumShards, int routingFactor) {
       int idHash = shardHash + 302; // Just random
       int routingValue = shardHash + Math.floorMod(idHash, ROUTING_PARTITION_SIZE);
       return Math.floorMod(routingValue, routingNumShards) / routingFactor;
  }
}