# spring-feign-clients
Showing how to use multiple Feign Clients using different Authorization headers.

## Providing credentials

Use environment variables to provide the credentials needed to access Jira and Slack APIs:
- `JIRA_API_USERNAME`
- `JIRA_API_PASSWORD`
- `SLACK_AUTH_TOKEN`

## Configure Jira/Zephyr API Base URL

In `application.yml` define the correct URL for the Jira/Zephyr API that you need to access:
```
zephyr:
  api:
    baseUrl: https://jira.my-company.com/jira/rest/zapi/latest
```
