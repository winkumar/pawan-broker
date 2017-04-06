package com.flycatcher.pawn.broker.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;




//import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * <h1>Meta Tenant Persistence Configuration</h1>
 * This is use for maintain Meta database configuration.
 * 
 * 
 * @author kumar
 * @version 1.0.0
 * @since 08-10-2016
 * 
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.flycatcher.pawn.broker.repo" }, entityManagerFactoryRef = "globalEntityManagerFactory", transactionManagerRef = "globalTransactionManager")
public class MetaPersistenceConfig {
	
	
	@Value("${flycatcher.meta.database.driverClass}")
	private String driverClassName;
	
	@Value("${flycatcher.meta.database.url}")
	private String url;
	
	@Value("${flycatcher.meta.database.userName}")
	private String username;
	
	@Value("${flycatcher.meta.database.password}")
	private String password;
	
	@Value("${flycatcher.meta.database.dialect}")
	private String dialect;
	
	@Value("${flycatcher.database.loadMetaData}")
	private String loadMetaData;
		
	@Value("${flycatcher.meta.maxPoolSize}")
	private Integer maxPoolSize;
	
	@Value("${flycatcher.meta.maxIdleTimeInMillis}")
	private Integer maxIdleTimeInMillis;
	
	@Value("${flycatcher.database.leakDetectionThreshold}")
	private Integer leakDetectionThreshold;
	
	@Value("${flycatcher.database.connectionTimeoutMs}")
	private Integer connectionTimeoutMs;
	
			
	/**
	 * This method use to set LocalContainerEntityManagerFactoryBean for meta database.
	 * @return  LocalContainerEntityManagerFactoryBean , this contain datasource ,persistence unit name and jpa vendor properties.
	 */
    @Bean(name="globalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean metaEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(metaDataSource());
        em.setPackagesToScan("com.flycatcher.pawn.broker.model");
        em.setPersistenceUnitName("flycatcher_db");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    /**
     * This method return datasource object for hikariCP connection pool.
     * @return datasource
     */
    @Bean(destroyMethod = "")
    public DataSource metaDataSource() {
    	
         
    	//Hikari datasource
    	
    	 final HikariDataSource dataSource = new HikariDataSource();
    	 if(maxPoolSize==null){
    		 dataSource.setMaximumPoolSize(1);
    		 dataSource.setMinimumIdle(1);
    	 }else{
    		 dataSource.setMaximumPoolSize(maxPoolSize);
    		 dataSource.setMinimumIdle(1);
    	 }
    	 
    	 if(maxIdleTimeInMillis==null){
    		 	dataSource.setIdleTimeout(10000);
    		 	
    	 }else{
    	 		dataSource.setIdleTimeout(maxIdleTimeInMillis);
    	 }
    	 
    	 if(leakDetectionThreshold==null){
				dataSource.setLeakDetectionThreshold(10000);
    	 }else{
				dataSource.setLeakDetectionThreshold(leakDetectionThreshold);
    	 }
    	 
    	 if(connectionTimeoutMs==null){
				dataSource.setConnectionTimeout(10000);
		 }else{
				dataSource.setConnectionTimeout(connectionTimeoutMs);
		 }
			
    	 
    	 dataSource.setPoolName("SPARROW_GLOBAL_CONNECTION_POOL");
    	 dataSource.setDriverClassName(driverClassName);
    	 dataSource.setJdbcUrl(url);
    	 dataSource.setUsername(username);
    	 dataSource.setPassword(password);
    	
    	    
        return dataSource;
    }
    
    /**
     * This method maintain transaction for the datasource.
     * @param emf
     * @return transactionManager, this will tell transaction state like commit or rollback.
     */
    @Bean(name="globalTransactionManager")
    public PlatformTransactionManager metaTransactionManager(@Qualifier("globalEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    /**
     * This method translate any exception happen in transaction.
     * @return transaction exception
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    /**
     *This method will retun additional properties for jpa hibernate vendor. 
     * @return properties, it will contain dialet, meta load data.
     */
    public Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.dialect",dialect );
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", loadMetaData);
        properties.put("hibernate.enable_lazy_load_no_trans", true);
        
        
        return properties;
    }
}
