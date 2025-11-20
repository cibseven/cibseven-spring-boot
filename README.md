# cibseven-spring-boot

## Fix for version 2.1.0
In version 2.1.0 we have a problem with the oauth2 authentication configuration on engine-rest.
On every load of the webapp you see a SystemException because the request to fetch available engines is missing.
<img width="1114" height="357" alt="grafik" src="https://github.com/user-attachments/assets/ac1788a3-aee2-4276-a89d-d9159b21fa6c" />

Inside this project is [src/main/java/org/cibseven/examples/springboot/config/EngineRestSecurityFilterChainFix.java](src/main/java/org/cibseven/examples/springboot/config/EngineRestSecurityFilterChainFix.java) which fixes this issue.
Copy this file in your application and the error disappears.
