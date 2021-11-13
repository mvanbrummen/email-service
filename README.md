# Email Service

Email service built with SpringBoot and Java 17. Uses Mailgun and SendGrid as email providers as primary and fallback.

Deployed to AWS as ECS Fargate service with CDK.

# Getting Started

Build and test the app (docker build adds hoverfly cert otherwise build will error. See TODO section)
```
cd email-service
DOCKER_BUILDKIT=1 docker build -t email-service .
```

Run locally passing in your API keys
```
docker run -p 8080:8080 \
	-e MAILGUN_API_KEY=$MAILGUN_API_KEY \
	-e SENDGRID_API_KEY=$SENDGRID_API_KEY \
	emailer
```

Send an email
```
curl http://localhost:8080/email/send \
	-H 'Content-Type: application/json' \
	-d '{	"from": {		 "email": "michaelvanbrummen@icloud.com",    "name": "Michael Van Brummen"	},		"to": [		{			"name": "Michael",			"email": "michaelvanbrummen@gmail.com"					}	],	"subject": "Test from my springboot app",	"content": "Hi"}'
```

# Deployment

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

# TODO

TODO