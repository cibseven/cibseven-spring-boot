# cibseven-spring-boot

## Fix for version 2.1.0
In version 2.1.0 there is a problem with the oauth2 authentication configuration on engine-rest.
On every load of the webapp you see a SystemException because the request to fetch available engines is missing.
Inside this project is [src/main/java/org/cibseven/examples/springboot/config/EngineRestSecurityFilterChainFix.java](src/main/java/org/cibseven/examples/springboot/config/EngineRestSecurityFilterChainFix.java) which fixes this issue.
Copy this file in your application and the error should disappear.
