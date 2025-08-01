
@component:Calculator
@feature:Multiplication
@layer:WS
@release:1.2v
@iteration:Sprint-1.6
Feature: Multiplication of any two numbers


    @Regression
    @Sanity
  Scenario: Multiply any two numbers
    Given the input values of "Multiply":
      | number1 | number2 |
      | 352     | 23      |
    When the service is called "Multiply"
    Then validate the response of "Multiply":
      | results |
      | 8096    |


    @Regression
    @Sanity
  Scenario: Multiply any two decimal numbers
    Given the input values of "Multiply":
      | number1 | number2 |
      | 67      | 56      |
    When the service is called "Multiply"
    Then validate the response of "Multiply":
      | results |
      | 3752    |