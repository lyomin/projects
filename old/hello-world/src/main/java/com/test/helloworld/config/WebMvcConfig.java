package com.test.helloworld.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
@Configuration
public class WebMvcConfig implements ApplicationContextAware, WebFluxConfigurer {

    ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(this.context);
        resolver.setPrefix("classpath:templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        resolver.setCheckExistence(false);
        return resolver;

    }

    @Bean
    public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
        SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
        ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
        viewResolver.setTemplateEngine(thymeleafTemplateEngine());
        return viewResolver;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafReactiveViewResolver());
    }
    
    
    @Bean
    public MessageSource messageSource() {
    	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
   /* @Bean
    LocaleSupportConfig editConfig() {
    	return new LocaleSupportConfig();
    }*/
    
    @Bean
    @ConditionalOnMissingBean(name = WebHttpHandlerBuilder.LOCALE_CONTEXT_RESOLVER_BEAN_NAME)
    protected LocaleContextResolver createLocaleContextResolver() {
        return new com.test.helloworld.config.LocaleResolver();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.
          addResourceHandler("/swagger-ui/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
          .resourceChain(false);
    }

}
