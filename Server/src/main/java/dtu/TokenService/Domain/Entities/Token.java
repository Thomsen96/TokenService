package dtu.TokenService.Domain.Entities;

import java.util.UUID;

public class Token {

	private String customerId = null;
	private String tokenUuid = null;
	private Boolean tokenValidity;

	public Token(String customerId) {
		this.customerId = customerId;
		tokenUuid = UUID.randomUUID().toString();
		setValidToken(true);
	}
	
	public Token(Boolean tokenValidity) {
		tokenValidity = false;
	}

	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getUuid() {
		return tokenUuid;
	}

	public Boolean getValidToken() {
		return tokenValidity;
	}

	public void setValidToken(Boolean validToken) {
		this.tokenValidity = validToken;
	}


	@Override
	public String toString() {
		return "TUUID: " + tokenUuid + "\tCID: " + customerId + "\tValid: " + tokenValidity;
	}

}
