package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.SSOClient;
import net.bingosoft.oss.ssoclient.SSOConfig;
import net.bingosoft.oss.ssoclient.internal.Urls;
import net.bingosoft.oss.ssoclient.model.AccessToken;
import net.bingosoft.oss.ssoclient.model.Authentication;
import net.bingosoft.oss.ssoclient.servlet.AbstractLoginServlet;

import javax.net.ssl.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.security.cert.X509Certificate;

/**
 * Created by KAEL on 2017/5/10.
 */
public class LoginServlet extends AbstractLoginServlet {
    public static SSOClient client = null;
    @Override
    protected SSOClient getClient(ServletConfig servletConfig) throws ServletException {
        if(client == null){
            ignoreHttpsCer();
            SSOConfig config = new SSOConfig();
            config.setClientId("clientId");
            config.setClientSecret("clientSecret");

            // 这个地址需要在应用注册的时候填写
            String redirectUri = servletConfig.getServletContext().getContextPath()+"/ssoclient/login";
            config.setRedirectUri(redirectUri);
            // 省公安厅开发测试环境sso:http://114.67.33.50:7077/ssov3
            // 本地开发测试sso:http://10.200.84.30:8089/ssov3
            // 聆客测试环境sso:https://10.201.76.141/sso
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

    public class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         *
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            // TODO Auto-generated method stub
            return true;
        }
    }

    @Override
    protected String buildRedirectUri(HttpServletRequest req, HttpServletResponse resp) {
        return Urls.appendQueryString(super.buildRedirectUri(req, resp),"return_url","http://www.baidu.com");
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
    public static void ignoreHttpsCer(){
        // Create a trust manager that does not validate certificate chains  
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)  {

            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }
        } };

        // Install the all-trusting trust manager  
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
