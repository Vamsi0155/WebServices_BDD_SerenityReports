
@component:CountryInfo
@feature:CurrencyDetails
@layer:WS
@release:1.6v
@iteration:Sprint-1.5
Feature: As a user, I want to validate the currency name by using the currency code


      @Regression
      @Sanity
  Scenario Outline: validate the currency name by using code.
    Given the input values of "CurrencyDetails":
      | currencyCode |
      | <currency>   |
    When the service is called "CurrencyDetails"
    Then validate the response of "CurrencyDetails":
      | currencyName |
      | <results>    |

      Examples:
      | currency | results |
      | INR      | Rupees  |
      | USD      | Dollars |
      | EUR      | Euro    |