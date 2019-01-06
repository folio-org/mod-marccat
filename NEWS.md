## 1.2.0 2018-12-31
 * Improve Search Functionality.


  >Search API Layer:
 
  | METHOD |  URL                                               | DESCRIPTION                                                     |
  |--------|----------------------------------------------------|-----------------------------------------------------------------|
  | GET    | /marccat/bibliographic-record/:id                  | Return bib and auth records results                             |
  | GET    | /marccat/bibliographic-record/from-template/:id    | Return bib and auth records results                             |
  | POST   | /marccat/bibliographic-record                      | Return bib and auth records results                             |
  | DELETE | /marccat/bibliographic-record/unlock/:id           | Return bib and auth records results                             |
  | PUT    | /marccat/bibliographic-record/lock/:id             | Return bib and auth records results                             |
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | GET    | /marccat/browse                                    | Return bib and auth records results                             |
  | GET    | /marccat/next-page                                 | Return bib and auth records results                             |
  | GET    | /marccat/previous-page                             | Return bib and auth records results                             |
  | GET    | /marccat/headings-by-tag                           | Return bib and auth records results                             |
  |        |                                                    |                                                                 |
  | GET    | /marccat/document-count-by-id                      | Return bib and auth records results                             |
  |        |                                                    |                                                                 |
  | GET    | /marccat/bibliographic/fields/mandatory            | Return bib and auth records results                             |
  |        |                                                    |                                                                 |
  | GET    | /marccat/previous-page                             | Return bib and auth records results                             |
  | GET    | /marccat/previous-page                             | Return bib and auth records results                             |
  | GET    | /marccat/previous-page                             | Return bib and auth records results                             |
  | GET    | /marccat/previous-page                             | Return bib and auth records results                             |



## 1.1.0 2018-12-14
 * Improve Search Functionality.
 * Add Browse functionality.
 * Add Browse API.
 * Add Template microservices.
 * Add microservices for Bib records.
 * Add API for merge authority and bibliographic result. [MODMODCAT-93](https://issues.folio.org/browse/MODCAT-93)
 * Cleaning project and remove unused class.
 * Add permission to descriptor.
 * Add interface for okapi.
 * Fix Relator code. [MODMODCAT-93](https://issues.folio.org/browse/MODCAT-92)
 * Remove swagger API.
 
## 1.0.0 2018-10-31
 * Initial release
 * Search Functionality interface
