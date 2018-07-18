package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOConfig;
import net.bingosoft.oss.ssoclient.internal.HttpClient;
import net.bingosoft.oss.ssoclient.internal.Urls;
import net.bingosoft.oss.ssoclient.model.AccessToken;
import net.bingosoft.oss.ssoclient.model.Authentication;
import net.bingosoft.oss.ssoclient.servlet.AbstractLoginServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * Created by KAEL on 2017/5/10.
 */
public class LoginServlet extends AbstractLoginServlet {
    public static SSOClient client = null;
    @Override
    protected SSOClient getClient(ServletConfig servletConfig) throws ServletException {
        if(client == null){
            HttpClient.ignoreHttpsCer();
            SSOConfig config = new SSOConfig();
            config.setClientId("clientId");
            config.setClientSecret("clientSecret");
            config.setDefaultReturnUrl(servletConfig.getServletContext().getContextPath()+"/user.jsp");
            // 这个地址需要在应用注册的时候填写
            String redirectUri = servletConfig.getServletContext().getContextPath()+"/ssoclient/login";
            config.setRedirectUri(redirectUri);
            String sso = servletConfig.getInitParameter("ssoUrl");
            config.autoConfigureUrls(sso);
            config.setLogoutUri(servletConfig.getServletContext().getContextPath()+"/logout");
            SSOClient client = new SSOClient(config);
            LoginServlet.client = client;
        }
        return client;
    }

    @Override
    protected void redirectToSSOLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        URI uri = URI.create(req.getRequestURL().toString());
        String logoutUri = uri.getScheme()+"://"+uri.getHost()+(uri.getPort()>0?(":"+uri.getPort()):"")+req.getContextPath()+"/logout";
        client.getConfig().setLogoutUri(logoutUri);
        super.redirectToSSOLogin(req, resp);
    }

    @Override
    protected void localLogin(HttpServletRequest request, 
                              HttpServletResponse response,
                              Authentication authentication,
                              AccessToken accessToken) throws ServletException, IOException {
        // 完成本地登录
        request.getSession().setAttribute("loginUser",authentication);
        // 保存用户访问令牌
        request.getSession().setAttribute("accessToken",accessToken);
    }
}
