Feature:
  Transfer Money

  Scenario: TC01 Transferring Money to receiver account
    Given I am on the login page
    When I enter username and password
    And I click the login button
    Then I should be logged in successfully
    And user clicks open money transfer buton
    When I click Transfer Money buton
    Then I see Sender account and Receiver Account texts
    And I select a random receiver account
    And I put data "100" amount text box
    Then click send button and check amount