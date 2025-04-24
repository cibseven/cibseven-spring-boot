# cibseven-spring-boot
## 🔐 Authentication Setup

To enable secure authentication, follow these steps:

1. **Create a properties file** in our Spring Boot project:
   
[cibseven-spring-boot/src/main/resources](src/main/resources)/cibseven-webclient.properties

3. **Add the following property**, replacing `SECRET` with the jwtSecret defined in the webclient project:
   
```authentication.jwtSecret=SECRET```

🔑 The SECRET value should match the one configured in:
[cibseven-webclient-web/src/main/resources](https://github.com/cibseven/cibseven-webclient/tree/main/cibseven-webclient-web/src/main/resources)/cibseven-webclient.yaml

This ensures that both webclient and spring-boot services use the same token secret for secure JWT validation.

✅ Note:
The file cibseven-webclient.properties is included in .gitignore and should not be committed to version control.
