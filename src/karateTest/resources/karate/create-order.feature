Feature: Create an order

  Background:
    * url 'http://localhost:8080'

  Scenario: Create a valid order successfully
    Given path '/api/orders'
    And request
      """
        {
          "customerReference": "CUSTOMER-2",
          "items": [
            {
              "productReference": "BOOK",
              "quantity": 4,
              "unitPrice": 5.00
            }
          ]
        }
      """
    When method post
    Then status 201
    And match response.status == 'PENDING'
    And match response.totalAmount == 20.00


  Scenario: Reject an order with no items
    Given path '/api/orders'
    And request
      """
      {
        "items": []
      }
      """
    When method post
    Then status 400