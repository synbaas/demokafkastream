package com.atossyntel.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

//import lombok.Data;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfiguration {
	
	@Autowired private KafkaProperties kafkaProperties;
	
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
       // props.put(StreamsConfig.STATE_DIR_CONFIG, 
        		//  TestUtils.tempDirectory().getAbsolutePath());
       // props.put(JsonDeserializer.DEFAULT_KEY_TYPE, String.class);
        //props.put(JsonDeserializer.DEFAULT_VALUE_TYPE, Test.class);
        return new StreamsConfig(props);
    }
	
    @Bean
    public KStream<String, String> kStreamJson(StreamsBuilder builder) {
    	KStream<String, String> textLines  = builder.stream("streams-plaintext-input");
    	Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
    	
		/*
		 * KTable<String, Test> combinedDocuments = stream .map(new
		 * TestKeyValueMapper()) .groupByKey() .reduce(new TestReducer(),
		 * Materialized.<String, Test, KeyValueStore<Bytes,
		 * byte[]>>as("streams-json-store")) ;
		 * 
		 * combinedDocuments.toStream().to("streams-json-output",
		 * Produced.with(Serdes.String(), new JsonSerde<>(Test.class)));
		 */
    	KStream counts  = textLines.flatMapValues(value-> Arrays.asList(pattern.split(value.toLowerCase())))
                .map((key, value) -> new KeyValue<Object, Object>(value, value))
                .filter((key, value) -> (!value.equals("the")))
                .groupByKey()
                .count().mapValues(value->Long.toString(value)).toStream();
    	
        counts.to("streams-wordcount-output");
        
        
        
        return null;
    }
    
	/*
	 * public static class TestKeyValueMapper implements KeyValueMapper<String,
	 * Test, KeyValue<String, Test>> {
	 * 
	 * @Override public KeyValue<String, Test> apply(String key, Test value) {
	 * return new KeyValue<String, KafkaStreamsConfiguration.Test>(value.key,
	 * value); }
	 * 
	 * }
	 */
	/*
	 * public static class TestReducer implements Reducer<Test> {
	 * 
	 * @Override public Test apply(Test value1, Test value2) {
	 * value1.getWords().addAll(value2.getWords()); return value1; }
	 * 
	 * }
	 * 
	 * @Data public static class Test {
	 * 
	 * private String key; private List<String> words;
	 * 
	 * public Test() { words = new ArrayList<>(); }
	 * 
	 * }
	 */
}