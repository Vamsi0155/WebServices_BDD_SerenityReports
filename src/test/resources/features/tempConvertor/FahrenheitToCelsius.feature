
@component:TempConvertor
@feature:FahrenheitToCelsius
@layer:WS
@release:1.4v
@iteration:Sprint-1.8
Feature: Conversion of the temperature fahrenheit to celsius

  @Regression
  @Sanity
  Scenario: Convert the temperature fahrenheit to celsius
    Given the input values of "ToCelsius":
      | fahrenheit |
      | 300        |
    When the service is called "ToCelsius"
    Then validate the response of "ToCelsius":
      | results              |
      | 148.888888888889     |