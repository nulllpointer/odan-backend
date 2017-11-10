package com.odan.security.user.model;


import com.odan.common.shared.model.AbstractEntity;
import com.odan.common.model.Flags.UserKind;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name= "app_user" )
public class User extends AbstractEntity{


	@Column(name="first_name")
	private String firstName;

	@Column(name="user_name")
	private String userName;

	@Column(name="user_password")
	private String userPassword;

	@Column(name="hero")
	private String hero;

	@Column(name="last_name")
	private String lastName;

	@Column(name="email")
	private String email;
	
	@Column(name="phone")
	private String phone;

	@Column(name="address_line1")
	private String addressLine1;

	@Column(name="address_line2")
	private String addressLine2;

	@Column(name="city")
	private String city;

	@Column(name="state")
	private String state;
	
	@Column(name="country")
	private String country;

	@Column(name="postal_code")
	private String postalCode;
	

	@Column(name="password")
	private String password;

	@Column(name="type")
	private UserKind type;

	public UserKind getType() {
		return type;
	}

	public void setType(UserKind type) {
		this.type = type;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getHero() {
		return hero;
	}

	public void setHero(String hero) {
		this.hero = hero;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
