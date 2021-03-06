## 2.2.5 2019-10-09
* Added sql patches

## 2.2.0 2019-10-02
* Fix permissions

## 2.1.5 2019-10-01
* Added check on configurations

## 2.0.0 2019-09-27
* Initialization template for cataloging.
* Fix log level for tenant.


## 1.9.0 2019-09-11
* Database initialization via JDBC with role, database, user and objects creation.
* Check role and database existence.


## 1.8.5 2019-07-31
* Implementation TenantAPI.
* Database initialization with role, database, user and objects creation.
* Configurations initialization.
* Existence check schema and configurations for datasource.
* Implement new call for pack_sortform database procedure.
* Fix wrong value in the Json verificationLevel.


## 1.8.0 2019-07-15
* Add tenants interface to ModuleDescriptor.json


## 1.5.0 2019-05-06
* Fix "No update" of full cache after edit bibliographic record and add new tag
* Add Creation of Name-Title heading behind the scene
* Add autosuggestion service tags in cataloguing workflow
* Bug fix save record in full_cache, edit and delete tags
* Bug fix in configuration properties for tags 006-007-008
* Bug fix in services for tags 006-007-008

 
## 1.2.0 2019-01-08
 * Fix Search Functionality.
 * Fix Browse functionality.
 * Merge template (association of a new template to a record)
 * Recovery categories in the save phase by tag number and indicators
 * Dropdown material type
 * Dropdown type code for positional tags, starting from the tag number
 * Fix articfact v1.1.0.[MODCAT-97](https://issues.folio.org/browse/MODCAT-97) and [MODCAT-99](https://issues.folio.org/browse/MODCAT-99)
 * Dropdown type code for positional tags, starting from the tag number
 * Create stub microservices for HRDI
 * Create microservices for db customization
 * Cleaning project and remove unused class.


## 1.1.0 2018-12-14
 * Improve Search Functionality.
 * Add Browse functionality.
 * Add Browse API.
 * Add Template microservices.
 * Add microservices for Bib records.
 * Add API for merge authority and bibliographic result. [MODCAT-93](https://issues.folio.org/browse/MODCAT-93)
 * Cleaning project and remove unused class.
 * Add permission to descriptor.
 * Add interface for okapi.
 * Fix Relator code. [MODCAT-93](https://issues.folio.org/browse/MODCAT-92)
 * Remove swagger API.
 
 
## 1.0.0 2018-10-31
 * Initial release
 * Search Functionality interface
