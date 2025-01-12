Feature:
  Add Money

  Scenario:
    Given I am on the login page
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully
    And user clicks open money transfer buton
    When I click add money buton
    And I put datas to required fields and add amount "500"
    And click add buton
