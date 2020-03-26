package edu.praveen.mobileappws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;


public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null ||  !( header.startsWith(SecurityConstants.TOKEN_PREFIX))) {
			chain.doFilter(req, resp);
			
			System.out.println("request Header is "+ header);

			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(req, resp);

	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {

		String token = req.getHeader(SecurityConstants.HEADER_STRING);

		if (token != null) {

			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			
			System.out.println("token is : "+token );

			String user = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token).getBody()
					.getSubject();
			
			if(user!=null) {
				return new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
			}
			return null;
		}
		return null;
	}

}
