
@component:TempConvertor
@feature:CelsiusToFahrenheit
@layer:WS
@release:1.4v
@iteration:Sprint-1.8
Feature: Conversion of the temperature celsius to fahrenheit

  @Regression
  @Sanity
  Scenario: Convert the temperature celsius to fahrenheit
    Given the input values of "ToFahrenheit":
      | celsius |
      | 45      |
    When the service is called "ToFahrenheit"
    Then validate the response of "ToFahrenheit":
      | results |
      | 113     |