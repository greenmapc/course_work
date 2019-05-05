package ru.itis.teamwork.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.itis.teamwork.services.TelegramService;
import ru.itis.teamwork.util.githubApi.GitHubApi;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"ru.itis.teamwork.services"})
@EnableJpaRepositories("ru.itis.teamwork.repositories")
@Import({WebSecurityConfig.class, RabbitConfig.class})
@PropertySource({"classpath:/db.properties",
        "classpath:/git.properties",
        "classpath:/rabbitmq.properties"})
public class RootConfig {
    @Resource
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.user"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        return dataSource;
    }

    @Bean
    public GitHubApi gitHubApi() {
        GitHubApi gitHubApi = new GitHubApi();

        gitHubApi.setCLIENT_ID(env.getRequiredProperty("CLIENT_ID"));
        gitHubApi.setCLIENT_SECRET(env.getRequiredProperty("CLIENT_SECRET"));
        //gitHubApi.setGITHUB(env.getRequiredProperty("GITHUB_API"));
        gitHubApi.setGITHUB_API_AUTH(env.getRequiredProperty("GITHUB_API_AUTH"));
        gitHubApi.setREDIRECT(env.getRequiredProperty("REDIRECT"));
        gitHubApi.setHttpClient(HttpClients.createDefault());

        return gitHubApi;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty("entitymanager.packages.to.scan"));
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.enable_lazy_load_no_trans", env.getRequiredProperty("hibernate.enable_lazy_load_no_trans"));

        return properties;
    }

    @Bean
    public TelegramService telegramService() {
        Integer port = env.getRequiredProperty("rabbit.host.port", Integer.class);
        String host = String.format("http://%s", env.getRequiredProperty("rabbit.host"));
        String queueName = env.getRequiredProperty("rabbit.queue.in");
        return new TelegramService(host, port, queueName);
    }
}
