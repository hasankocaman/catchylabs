Feature: Create Account
  This feature allows users to define an account. Users can only open one account at a time, and the account balance cannot be less than zero.
  The initial account type is suggested to be a "Saving Account."
  Users can rename the account after creation.
  All accounts start with a fixed amount, and no currency is specified.



  @createAccount
  Scenario: TC01 Successful account creation
    Given I am on the login page
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully
    And user clicks open money transfer buton
    And user clicks create an account buton
    Given I am on the account creation page
    And the dropdown menu contains the "SAVING" and "CHECKING" account types
    When I select the "CHECKING" account type
    And I enter an account name
    Then the account should be created successfully
