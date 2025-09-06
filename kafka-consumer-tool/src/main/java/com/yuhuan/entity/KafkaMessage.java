package com.yuhuan.entity;

public class KafkaMessage {
    private final long timestamp;
    private final int partition;
    private final long offset;
    private final String key;
    private final String value;

    public KafkaMessage(long timestamp, int partition, long offset, String key, String value) {
        this.timestamp = timestamp;
        this.partition = partition;
        this.offset = offset;
        this.key = key;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPartition() {
        return partition;
    }

    public long getOffset() {
        return offset;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "timestamp=" + timestamp +
                ", partition=" + partition +
                ", offset=" + offset +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
