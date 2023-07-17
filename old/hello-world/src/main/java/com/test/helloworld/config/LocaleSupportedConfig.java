package com.test.helloworld.config;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.LocaleContextResolver;

@Component
public class LocaleSupportedConfig extends DelegatingWebFluxConfiguration {
    @Override
    protected LocaleContextResolver createLocaleContextResolver() {
        return new LocaleResolver();
    }
}
