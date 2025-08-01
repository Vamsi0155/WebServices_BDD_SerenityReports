
@component:Calculator
@feature:Division
@layer:WS
@release:1.2v
@iteration:Sprint-1.8
Feature: Division of any two numbers

    @Regression
    @Sanity
  Scenario: Division of any two numbers
    Given the input values of "Division":
      | number1 | number2 |
      | 350     | 20      |
    When the service is called "Division"
    Then validate the response of "Division":
      | results |
      | 18      |


    @Regression
    @Sanity
  Scenario: Division of any two decimal numbers
    Given the input values of "Division":
      | number1 | number2 |
      | 675     | 10      |
    When the service is called "Division"
    Then validate the response of "Division":
      | results |
      | 68      |