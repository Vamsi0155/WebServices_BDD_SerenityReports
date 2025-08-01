
@component:Calculator
@feature:Subtraction
@layer:WS
@release:1.2v
@iteration:Sprint-1.4
Feature: Subtraction of any two numbers

    @Regression
    @Sanity
  Scenario: Subtract any two numbers
    Given the input values of "Subtraction":
      | number1 | number2 |
      | 352     | 233     |
    When the service is called "Subtraction"
    Then validate the response of "Subtraction":
      | results |
      | 119     |


    @Regression
    @Sanity
  Scenario: Subtract any two decimal numbers
    Given the input values of "Subtraction":
      | number1 | number2 |
      | 675     | 567     |
    When the service is called "Subtraction"
    Then validate the response of "Subtraction":
      | results |
      | 108     |