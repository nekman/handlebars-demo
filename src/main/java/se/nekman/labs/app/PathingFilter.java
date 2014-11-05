package se.nekman.labs.app;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;


/**
 * http://stackoverflow.com/a/16747253/141363
 *
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