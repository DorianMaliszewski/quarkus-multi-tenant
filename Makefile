
# Compile
compile:
	cd user-service && ./mvnw clean package compile

launch:
	cd user-service && ./mvnw clean package compile && cd ../ && docker-compose up -d

start:
	docker-compose start

build:
	docker-compose build

stop:
	docker-compose stop

down:
	docker-compose down

up:
	docker-compose up -d

logs:
	docker-compose logs -f