package com.test.helloworld.config;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.h2.CloseableConnectionFactory;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.proxy.core.QueryExecutionInfo;
import io.r2dbc.proxy.listener.ProxyExecutionListener;
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter;

@Configuration
@EnableR2dbcRepositories
public class DatabaseConfig extends AbstractR2dbcConfiguration {

	public boolean doQueryLogging = false;
	
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        //ConnectionFactory factory = ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

        //see: https://github.com/spring-projects/spring-data-r2dbc/issues/269
//        return new H2ConnectionFactory(
//                H2ConnectionConfiguration.builder()
//                        //.inMemory("testdb")
//                        .file("./testdb")
//                        .username("user")
//                        .password("password").build()
//        );
    	
    	LoggingListener logger = new LoggingListener();

    	if (doQueryLogging == false) return getH2ConncetionFactory();
    	
    	ConnectionFactory connectionFactory = ProxyConnectionFactory
    			.builder(getH2ConncetionFactory())
    		    .onAfterQuery(logger::afterQuery)
    		    .build();
    	
        return connectionFactory;
    }
    
    private ConnectionFactory getH2ConncetionFactory() {
    	ConnectionFactory factory = ConnectionFactories.get("r2dbc:h2:mem://sa:sa@/test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

        //see: https://github.com/spring-projects/spring-data-r2dbc/issues/269
        /*return new H2ConnectionFactory(
                 H2ConnectionConfiguration.builder()
                        .inMemory("test")
                //.file("./testdb")
                        .username("sa")
                        .password("").build()
        );*/
    	//CloseableConnectionFactory factory = H2ConnectionFactory.inMemory("test");
    	return factory;
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
    
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("sql/data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
    
    public static class LoggingListener implements ProxyExecutionListener {

    	  private static final Logger logger = LoggerFactory.getLogger(LoggingListener.class);

    	  private final QueryExecutionInfoFormatter formatter = QueryExecutionInfoFormatter.showAll();

    	  @Override
    	  public void afterQuery(QueryExecutionInfo execInfo) {
    	    logger.info(this.formatter.format(execInfo));
    	  }

    	}
}   
