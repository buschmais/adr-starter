package com.salesmanager.shop.store.api.v1.customer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.store.security.AuthenticationRequest;
import com.salesmanager.shop.store.security.AuthenticationResponse;
import com.salesmanager.shop.store.security.JWTTokenUtil;
import com.salesmanager.shop.store.security.user.JWTUser;
import com.salesmanager.shop.utils.LanguageUtils;

@Controller
@RequestMapping("/api/v1")
public class AuthenticateCustomerApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateCustomerApi.class);

    @Value("${authToken.header}")
    private String tokenHeader;

    @Inject
    private AuthenticationManager jwtCustomerAuthenticationManager;

    @Inject
    private JWTTokenUtil jwtTokenUtil;

    @Inject
    private UserDetailsService jwtCustomerDetailsService;
    
	@Inject
	private CustomerFacade customerFacade;
	
	@Inject
	private StoreFacade storeFacade;
	
	@Inject
	private LanguageUtils languageUtils;
    
	/**
	 * Create new customer for a given MerchantStore, then authenticate that customer
	 */
	@RequestMapping( value={"/auth/register"}, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<?> register(@Valid @RequestBody PersistableCustomer customer, HttpServletRequest request, HttpServletResponse response, Device device) throws Exception {

		
		
		try {
			
			MerchantStore merchantStore = storeFacade.getByCode(com.salesmanager.common.business.constants.Constants.DEFAULT_STORE);
			Language language = languageUtils.getRESTLanguage(request, merchantStore);	
			
			
			
			customerFacade.registerCustomer(customer, merchantStore, language);
			
	        // Perform the security
	    	Authentication authentication = null;
	    	try {
	    		
	    		authentication = jwtCustomerAuthenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(
	                            customer.getUserName(),
	                            customer.getClearPassword()
	                    )
	            );
	    		
	    	} catch(Exception e) {
	    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	}
	    	
	    	if(authentication == null) {
	    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	}

	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Reload password post-security so we can generate token
	        final JWTUser userDetails = (JWTUser)jwtCustomerDetailsService.loadUserByUsername(customer.getUserName());
	        final String token = jwtTokenUtil.generateToken(userDetails, device);

	        // Return the token
	        return ResponseEntity.ok(new AuthenticationResponse(customer.getId(),token));

			
		} catch (Exception e) {
			LOGGER.error("Error while registering customer",e);
			try {
				response.sendError(503, "Error while registering customer " + e.getMessage());
			} catch (Exception ignore) {
			}
			
			return null;
		}

		
	}

	/**
	 * Authenticate a customer using username & password
	 * @param authenticationRequest
	 * @param device
	 * @return
	 * @throws AuthenticationException
	 */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
    	Authentication authentication = null;
    	try {
    		
	
        		//to be used when username and password are set
        		authentication = jwtCustomerAuthenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticationRequest.getUsername(),
                                authenticationRequest.getPassword()
                        )
                );

    		
    	} catch(Exception e) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	
    	if(authentication == null) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        // todo create one for social
        final JWTUser userDetails = (JWTUser)jwtCustomerDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(userDetails.getId(),token));
    }

    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JWTUser user = (JWTUser) jwtCustomerDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(user.getId(),refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
