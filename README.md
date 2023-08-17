# Cangvel

Application that analyses and rates CVs from email.

## Run

To run the application, clone this repository and run the following command in a downloaded directory:

```shell
mvn clean package
java -jar target/Cangvel-0.0.1-SNAPSHOT.jar
```

Alternatively, you can download the image from a docker repository and run it in the container:

```shell
docker pull lukrzak/cangvel
docker run --env MAIL_USER={user} --env MAIL_PASS={pass} --env SAVE_PATH={path} lukrzak/cangvel
```

Where {user}, and {pass} are placeholders for your gmail credentials. Placeholder {path} is an absolute path to the directory where accepted files will be stored.
Remember to use a password generated for applications in Google Account (not real gmail password). You can do this in My Account > Security > App Passwords (only when 2-step verification is enabled) > Generate password.

## Usage

After proper parameter configuration, application will analyse your inbox automatically, every 10 seconds.

## Known issues

- Attachments are continously saved after being accepted, resulting in file duplication.

## Planned features

- Requirements passed as a configuration file.
- Evaluator based on a built decision tree.
