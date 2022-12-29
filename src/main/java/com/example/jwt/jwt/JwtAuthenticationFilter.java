package com.example.jwt.jwt;

import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password로 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을 함.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //AuthenticationManger를 받았기에 이를 통해 로그인 시도
    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수이다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");
        // 1. username, password 받아서
        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while((input = br.readLine()) != null) {
//                System.out.println(input);
//            }
            // id , pw 확인

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);
            // json으로 요청할 때는 ObjectMapper를 통해 Parsing 해주면 된다.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            System.out.println(request.getInputStream().toString()); // request.getInputStream 안에 ID, PW가 담겨있다.

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨.
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=============================");
        // 2. 정상인지 로그인 시도를 해본다. authenticationManager로 로그인 시도를 하면 !!
        // PrincipalDetailsService 가 호출이 된다. loadUserByUsername() 함수 실행 됨.

        // 3. PrincipalDetails를 세션에 담고 -> 이를 세션에 담지 않으면 권한관리가 되지 않는다.

        // 4. JWT 토큰을 만들어서 응답해주면 된다.

        return super.attemptAuthentication(request, response);
    }
    //이 때 실행되는 함수 attemptAuthentication 함수이다.

}
