package aysha.abatova.currencyconverter.components;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKeyHeader = request.getHeader("Api-Key");
        

        if (apiKeyHeader == null || !apiKeyHeader.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // actually should be unauthorized, but requirements
           // response.sendError(0);
            return false;
        }

        return true;
    }

    
    
}
