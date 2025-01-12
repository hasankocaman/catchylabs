Feature:
  Edit Account

  Scenario: TC01 Successful account edit
    Given I am on the login page
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully
    And user clicks open money transfer buton
    When I click edit account buton
    And I click update buton and edit the account name
    Then verify that the user could update the account name
