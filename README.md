# SOAP User Service
Этот проект представляет собой простой SOAP-сервис для управления пользователями и их ролями

## Сборка проекта
Для сборки проекта у Вас должен быть установлен Apache Maven  
Перейдите в корневую директорию проекта и выполните следующую команду:
```bash
mvn clean package -DskipTests
```

После успешной сборки, jar-файл будет находиться в директории `target/`

## Запуск приложения
Вы можете запустить Spring Boot-приложение, используя Java 17:
```bash
java -jar target/soap-user-service-0.0.1-SNAPSHOT.jar
```

## SOAP API
Все запросы отправляются методом `POST` на эндпоинт `http://localhost:8080/ws`  
Необходимо добавить заголовок `Content-Type: text/xml`  
Используемая xsd-схема расположена по пути `src/main/resources/xsd/users.xsd`  
Ниже приведены примеры запросов, отправляемых с использованием утилиты curl в bash (в Windows PowerShell не сработает, но можно взять Git Bash)   

### Получение всех пользователей (без ролей)
```bash
curl -X POST http://localhost:8080/ws \
     -H "Content-Type: text/xml" \
     -d '<soapenv:Envelope
          xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
          xmlns:dto="http://codemark.ru/soap/dto">
     <soapenv:Header/>
     <soapenv:Body>
        <dto:getAllUsersRequest/>
     </soapenv:Body>
     </soapenv:Envelope>'
```

### Получение пользователя по логину (с ролями)
```bash
curl -X POST http://localhost:8080/ws \
     -H "Content-Type: text/xml" \
     -d '<soapenv:Envelope 
          xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
          xmlns:dto="http://codemark.ru/soap/dto">
     <soapenv:Header/>
     <soapenv:Body>
        <dto:getUserRequest>
          <dto:userName>ivan_admin</dto:userName>
        </dto:getUserRequest>
     </soapenv:Body>
     </soapenv:Envelope>'
```

### Добавление нового пользователя
```bash
curl -X POST http://localhost:8080/ws \
     -H "Content-Type: text/xml" \
     -d '<soapenv:Envelope
          xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
          xmlns:dto="http://codemark.ru/soap/dto">
     <soapenv:Header/>
     <soapenv:Body>
        <dto:createUserRequest>
          <dto:user>
            <dto:userName>gost_guest</dto:userName>
            <dto:firstName>Gost</dto:firstName>
            <dto:lastName>Gostev</dto:lastName>
            <dto:password>GuestPass123</dto:password>
            <dto:roles>
              <dto:role>guest</dto:role>
            </dto:roles>
          </dto:user>
        </dto:createUserRequest>
     </soapenv:Body>
     </soapenv:Envelope>'
```