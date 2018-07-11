package net.bingosoft.demo;

import net.bingosoft.oss.ssoclient.internal.Base64;
import net.bingosoft.oss.ssoclient.internal.JWT;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author kael.
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Map<String, Object> map = JWT.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjbGllbnRJZCIsInN1YiI6IjExMTExMSIsImV4cCI6MTQ5NjY1NTkwMSwibmFtZSI6IuW9rem4v-adsCIsImxvZ2luX25hbWUiOiJwZW5naGoifQ.mHA45xyK05C5IarYOUS7NRSUr13u5qxiP1WFJP_uNJM","clientSecret");
        map.forEach((s, o) -> System.out.println("k:"+s+",v:"+o));
        String json = new String(Base64.urlDecode("eyJhdWQiOiJjbGllbnRJZCIsInN1YiI6IjExMTExMSIsImV4cCI6MTQ5NjY1NTkwMSwibmFtZSI6IuW9rem4v-adsCIsImxvZ2luX25hbWUiOiJwZW5naGoifQ=="), JWT.UTF_8);
        System.out.println(json);
        System.out.println("eyJhdWQiOiJjbGllbnRJZCIsInN1YiI6IjExMTExMSIsImV4cCI6MTQ5NjY1NTkwMSwibmFtZSI6IuW9rem4v-adsCIsImxvZ2luX25hbWUiOiJwZW5naGoifQ".length());
        System.out.println(122%4);
    }
}
