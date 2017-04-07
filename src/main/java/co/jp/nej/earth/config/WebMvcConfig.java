package co.jp.nej.earth.config;

import java.util.Properties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@EnableWebMvc
class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages";
	private static final String VIEWS = "/WEB-INF/views/";

	private static final String RESOURCES_LOCATION = "/resources/";
	// private static final String RESOURCES_HANDLER = RESOURCES_LOCATION +
	// "**";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_SOURCE);

		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	@Bean(name = "freemarkerConfig")
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("/WEB-INF/views/");
		Properties settings = new Properties();
		settings.put("auto_import", "/common/standardPage.ftl as standard");
		configurer.setFreemarkerSettings(settings);
		configurer.setDefaultEncoding("UTF-8");
		return configurer;
	}

	@Bean
	public ViewResolver viewResolver() {
		FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();

		viewResolver.setCache(false);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".ftl");
		viewResolver.setRequestContextAttribute("rc");
		viewResolver.setContentType("text/html;charset=UTF-8");
		// viewResolver.setContentType("UTF-8");
		return viewResolver;
	}
}