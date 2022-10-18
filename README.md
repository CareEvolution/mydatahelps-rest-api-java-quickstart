
# MyDataHelps REST API Java Quickstart

This app is a demonstration of how to access the [MyDataHelps REST API](https://developer.mydatahelps.org/) using Java. It gives an example of obtaining an access token and making a simple query to the API. You can use this app for reference, or modify it to test out your own API requests in a development environment.

## Prerequisites

Before you begin, you will need three things:

* Your service account name, like “RKStudio.1234.test.”
* Your project ID, which is a GUID.
* The private key you associated with your service account.

For help finding this information, see the [MyDataHelps Developer Docs](https://developer.mydatahelps.org/api/quickstart.html).

## Using the App

This is a command-line application in Java. To run the app:

1. Clone this repository.
2. Edit the `src/main/java/Config.java` file and fill in your project ID, service account name, and private key. See **Prerequisites** above for how to get this information. Be sure to include the begin/end tags “--BEGIN RSA PRIVATE KEY--“ and “--END RSA PRIVATE KEY--" in your private key, and use line breaks, looking something like this:

```
PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n230703de230703de230703de\nb62b0e24b62b0e24b62b0e24\n...\n-----END RSA PRIVATE KEY-----"
```

3. Build the Java application and run the `QuickStart` class.

If successful, the app will print out your token and number of participants in the console, like so:

```
--------------------------
Obtained access token:
  YOUR TOKEN HERE
--------------------------
Total Participants: 5
--------------------------
```

The access token is only valid for a few minutes, but you can copy/paste it into a REST query tool of your choice to try out advanced queries.

## Troubleshooting

If you see an error when running the example application, double-check the information in the Prerequisites, particularly the format of the private key.

If you have trouble getting the example application to work, feel free to [contact MyDataHelps Support](https://developer.mydatahelps.org/help.html).
