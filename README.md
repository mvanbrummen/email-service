# Email Service

Email service built with SpringBoot and Java 17. Uses Mailgun and SendGrid as email providers as primary and fallback with Spring Retry providing the fallback mechanism.

Deployed to AWS as ECS Fargate service with CDK available here:

http://email-email-1icufvg0y5nkp-1380419665.ap-southeast-2.elb.amazonaws.com

------------------------------------------------------------------------------------------

#### Send Email 

<details>
 <summary><code>POST</code> <code><b>/email/send</b></code></summary>

##### Parameters

####Email Send Request

> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | from      |  required | Person   | Must be 'michaelvanbrummen@icloud.com' as I had to register a 'from email' in the providers  |
> | subject      |  required | String   | Email subject |
> | content      |  required | String  | Plain text email content  |
> | to      |  required |   Person[] |  List of recipients  |
> | cc      |  optional |   Person[] |  List of CC recipients  |
> | bcc      |  optional |   Person[] | List of BCC recipients  |

####Person
> | name      |  type     | data type               | description                                                           |
> |-----------|-----------|-------------------------|-----------------------------------------------------------------------|
> | email      |  required |   String | Valid email address  |
> | name      |  optional |   String |  Name of the person |
##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `202`         |         | None                                |
> | `400`         | `application/json`                |   `{"field_name":"validation error message"}`                          |
> | `502`         | `application/json`         |        `{"error": "Service is currently unavailable. Please try again later."}`                                                         |

##### Example cURL

> ```javascript
>  curl -X POST -H "Content-Type: application/json" --data @email-request.json http://localhost:8080/email/send
> ```

</details>

------------------------------------------------------------------------------------------

## Getting Started

Build and test the app (docker build adds hoverfly cert otherwise build will error with just mvn. See TODO section)
```
cd email-service
DOCKER_BUILDKIT=1 docker build -t email-service .
```

Run locally passing in your API keys
```
docker run -p 8080:8080 \
	-e MAILGUN_API_KEY=$MAILGUN_API_KEY \
	-e SENDGRID_API_KEY=$SENDGRID_API_KEY \
    email-service	
```

Send an email
```
curl http://localhost:8080/email/send \
	-H 'Content-Type: application/json' \
	-d '{	"from": {		 "email": "michaelvanbrummen@icloud.com",    "name": "Michael Van Brummen"	},		"to": [		{			"name": "Michael",			"email": "michaelvanbrummen@gmail.com"					}	],	"subject": "Test from my springboot app",	"content": "Hi"}'
```

## Deployment

Make sure AWS creds are setup
```
aws configure
```
and the API keys are set in the environment
```
export MAILGUN_API_KEY=$MAILGUN_API_KEY
export SENDGRID_API_KEY=$SENDGRID_API_KEY
```

then run cdk using supported nodejs version (14.5.0 is latest)
```
export DOCKER_BUILDKIT=1
cd iac
npm install
npm run build
npx cdk synth
npx cdk deploy
```

Call the API (the email providers restrict which emails you can actually send to/from so have to use the below)
```
curl http://email-email-1icufvg0y5nkp-1380419665.ap-southeast-2.elb.amazonaws.com/email/send \
	-H 'Content-Type: application/json' \
	-d '{	"from": {		 "email": "michaelvanbrummen@icloud.com",    "name": "Michael Van Brummen"	},		"to": [		{			"name": "Michael",			"email": "michaelvanbrummen@gmail.com"					}	],	"subject": "Test from my springboot app",	"content": "Hi"}'
```

## TODO

### Architecture
I would re-architect this system for resilience and fault tolerance by placing email requests on a queue. That way email requests could persist in an error queue if there are downstream issues with the email providers and the client can poll for the status.

Client flow could look like:
* POST request to send the email and get a 202 Accepted response code and a reference ID in the response body (or stick that URL in the Location header)
* GET request using the ID to poll the email request status 

### Application
* Add swagger API docs
* Add more logging to the app
* Add missing tests. Email gateways should have tests.
* Add missing test cases. Object mapping edge cases, controller failure cases.
* Improve exception error handling. Lots of missing exception cases eg invalid request JSON. Error message json could be more consistent too.

### Testing
* Add end to end acceptance test suite
* Add a load testing suite eg Gatling

### Deployment
* Increase ECS instance count and enable autoscaling
* Enable ECS deployment circuit breaker to rollback bad deploys

### Security
* Add auth to the application endpoints eg oauth2
* Store secrets securely eg SSM, secrets manager, hashicorp vault, spring config server etc. Currently set as ECS task image env vars which are visible in the AWS console.

### Monitoring/Alerting
* Add metrics to the retry functionality for observability. Resilience4j also has [retry functionality with actuator integration](https://reflectoring.io/retry-with-springboot-resilience4j/#actuator-endpoints) so could potentially use that instead.
