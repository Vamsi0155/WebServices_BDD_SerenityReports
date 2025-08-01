
@component:CountryInfo
@feature:CountryInfo
@layer:WS
@release:1.6v
@iteration:Sprint-1.5
Feature: As a user, I want to validate the All countries information


      @Regression
      @Sanity
  Scenario: validate the country information.
    Given the input values of "CountryInfo":
      | countryCode |
      | IND         |
    When the service is called "CountryInfo"
    Then validate the response of "CountryInfo":
      | country_name | capital_city | currency | flag    |
      | India        | New Delhi    | INR      | NotNull |