# Cangvel

Application that analyses and rates CVs from email.

## Run

To run application, clone repository and run following command in downloaded directory:

```shell
mvn clean package
java -jar target/Cangvel-0.0.1-SNAPSHOT.jar
```

Alternatively, you can download image from docker repository and run it in the container:

```shell
docker pull lukrzak/cangvel
docker run --env MAIL_USER={user} --env MAIL_PASS={pass} --env SAVE_PATH={path} lukrzak/cangvel
```

Where {user}, and {pass} are placeholders for you gmail credentials. Placeholder {path} is path, where accepted files
will be stored.
Remember to use password generated for applications in Google Account (not real gmail password). You can do this in My
Account > Security > App Passwords (only when 2-step verification is enabled) > Generate password.s

## Usage

After proper parameter configuration, application will analyse inbox automatically, every 10 seconds.

## Known issues

- Accepted attachment duplication.

## Planned features

- Requirements passed as configuration file.
- Evaluator based on built decision tree.
