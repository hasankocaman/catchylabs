@login
Feature: User Login Functionality

   As a user
  I want to login to the application
  So that I can acces my account

Background:
  Given I am on the login page

  @smoke @regression
  Scenario: TC01 Successful login with valid credentials
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully

@negative
  Scenario: TC02  Unsuccessful login with invalid credentials
    When I enter invalid username and password
    And I click the login button
    Then I should see an error message "Username or Password Invalid!"