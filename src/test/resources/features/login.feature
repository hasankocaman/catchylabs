@login
Feature: User Login Functionality

   As a user
  I want to login to the application
  So that I can acces my account


  @smoke @regression
  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully
