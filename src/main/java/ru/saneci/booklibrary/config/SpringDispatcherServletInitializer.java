package ru.saneci.booklibrary.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.EnumSet;

public class SpringDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // Этот метод использоваться не будет, поэтому возвращаем null.
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        // Указание "/" необходимо для того чтобы все HTTP-запросы приходили на DispatcherServlet
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);
        registerHiddenHttpMethodFilter(context);
        registerCharacterEncodingFilter(context);
    }

    private void registerHiddenHttpMethodFilter(ServletContext context) {
        Dynamic filterRegistration = context.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter());
        filterRegistration.addMappingForUrlPatterns(null, true, "/*");
    }

    private void registerCharacterEncodingFilter(ServletContext context) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        Dynamic filterRegistration = context.addFilter("characterEncodingFilter", characterEncodingFilter);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}
