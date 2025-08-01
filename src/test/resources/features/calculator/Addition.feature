
@component:Calculator
@feature:Addition
@layer:WS
@release:1.2v
@iteration:Sprint-1.2
Feature: As a user, I want to Add any of the two numbers

      @Regression
      @Sanity
  Scenario: Add any two numbers.
    Given the input values of "Addition":
    | number1 | number2 |
    | 352     | 233     |
    When the service is called "Addition"
    Then validate the response of "Addition":
    | results |
    | 585     |


    @Regression
    @Sanity
  Scenario: Add any two decimal numbers
    Given the input values of "Addition":
      | number1 | number2 |
      | 675     | 567     |
    When the service is called "Addition"
    Then validate the response of "Addition":
      | results |
      | 1242    |
