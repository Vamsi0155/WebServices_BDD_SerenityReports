
@component:CountryInfo
@feature:CountryDetails
@layer:WS
@release:1.6v
@iteration:Sprint-1.5
Feature: As a user, I want to validate the All country names by using ISO codes


      @Regression
      @Sanity
  Scenario Outline: validate the country name by using ISO code.
    Given the input values of "CountryName":
      | countryCode |
      | <country>   |
    When the service is called "CountryName"
    Then validate the response of "CountryName":
      | countryName |
      | <results>   |

    Examples:
      | country  | results        |
      | IND      | India          |
      | USA      | United States  |


  @Regression
    @Sanity
  Scenario Outline: validate the country ISO code by using name.
    Given the input values of "CountryISOCode":
      | countryName |
      | <country>   |
    When the service is called "CountryISOCode"
    Then validate the response of "CountryISOCode":
      | countryCode |
      | <results>   |

    Examples:
      | country         | results |
      | India           | IN      |
      | United States   | US      |