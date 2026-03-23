package com.example.flight.route.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.cache.CouchbaseCacheConfiguration;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;

import java.time.Duration;

@Configuration
public class CacheConfig {
    public static final String FIVE_MIN = "FIVE_MIN";


    @Bean
    public CouchbaseCacheManager cacheManager(CouchbaseClientFactory clientFactory) {
        return CouchbaseCacheManager.builder(clientFactory)
                .withCacheConfiguration(FIVE_MIN,
                        CouchbaseCacheConfiguration.defaultCacheConfig()
                                .entryExpiry(Duration.ofMinutes(5)))
                .build();
    }
}