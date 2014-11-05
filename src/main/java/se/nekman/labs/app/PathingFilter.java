package se.nekman.labs.app;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


/**
 * Want my REST-calls in root-context.
 * And also serve static content.
 * 
 * Needed this filter, found here: 
 * http://stackoverflow.com/a/16747253/141363 to get it to work.
 */
@WebFilter(urlPatterns = "/*")
public class PathingFilter implements Filter {
    Pattern[] restPatterns = {
       Pattern.compile("/person.*")
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String path = ((HttpServletRequest) request).getServletPath();
            for (Pattern pattern : restPatterns) {
                if (pattern.matcher(path).matches()) {
                    String newPath = "/rest/" + path;
                    request.getRequestDispatcher(newPath)
                        .forward(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}