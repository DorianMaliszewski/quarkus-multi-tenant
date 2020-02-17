# Quarkus multi tenant

## Prerequesite

- Maven
- Java OpenJDK 8 (or equivalent)
- Docker
- Docker-compose

## Launch the stack

- Quick launch for the first time with :

```bash
make launch
```

## Different make command

- `make compile` : Compile the user-service project
- `make launch` : Compile et launch the docker-compose stack
- `make {up|build|down|start|stop}` : Shortcut that does `docker-compose {up|build|down|start|stop}`
- `make logs` : Does `docker-compose logs -f` 

## Initialize keycloak 

Import the keycloak configuration files in config/keycloak for Realm Creation. It will create 3 realm :

- Realm default:
  - Clients : 
    - frontend | 4394f3cf-7418–4815-b0cb-00998fd7265d
    - user-service (for quarkus)
  - Users : dorian | dorian
- Realm example:
  - Clients : 
    - frontend | a306766d-52c2–4980–8218–72370fea62a2
    - user-service (for quarkus)
  - Users : dorian | dorian
- Realm example-b:
  - Clients : 
    - frontend | 78876dfb-036d-4715-bdc0–229fc1540753
    - user-service (for quarkus)
  - Users : dorian | dorian
  
## Get an access token

Change REALM_NAME and FRONTEND_CLIENT_SECRET by the information of the realm you want to connect :
```bash
export access_token=$(\
    curl -X POST http://localhost:8085/auth/realms/REALM_NAME/protocol/openid-connect/token \
    --user frontend:FRONTEND_CLIENT_SECRET \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=dorian&password=dorian&grant_type=password' | jq --raw-output '.access_token' \
 )
````

Example :

```bash
export access_token=$(\
    curl -X POST http://localhost:8085/auth/realms/default/protocol/openid-connect/token \
    --user frontend:4394f3cf-7418-4815-b0cb-00998fd7265d \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=dorian&password=dorian&grant_type=password' | jq --raw-output '.access_token' \
 )
```

## Get your information

If you get the token in the default realm :

```bash
curl -X GET -v http://localhost:8080/default/users/me \
-H "Authorization: Bearer $access_token"
```

Change in the path by the realm you connect in.

Try with the wrong realm, you get a 403 response.

## Troubleshooting

Sometime the user-service start to fast for keycloak, to try redo `make start` when keycloak server is up and ready

## Contact

You can contact me on maliszewskid3@gmail.com for more information

Medium Post : [https://medium.com/@maliszewskid3/implement-multi-tenancy-oidc-and-hibernate-on-quarkus-4f2e0214ed2d](Click here)