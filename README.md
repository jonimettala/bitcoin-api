# Bitcoin API

Bitcoin API allows checking certain Bitcoin information.

## Usage

The service has been built and tested using **Java 11**.

### Run Maven project  

`mvn spring-boot:run`

By default, the service is running at `http://localhost:8080`.

### API documentation

Found at `/bitcoin/api/v1/api-doc`.

## Operations

The following requests can be sent to the service.

### Get downward trend

Counts the longest downward trend in Bitcoin price during the range.

`GET /bitcoin/api/v1/downward?from=2020-01-01&to=2021-10-10`

**Expected response**
```json
{
  "description": "Longest downward trend (days)",
  "longestTrend": 8
}
```

### Get the highest trading volume

Counts the highest trading volume during the range.

`GET /bitcoin/api/v1/highest?from=2021-09-20&to=2021-12-21`

**Expected response**
```json
{
  "description": "Highest volume",
  "date": "2021-11-08",
  "volume": 90990605009.20009,
  "currency": "EUR"
}
```

### Get the best buy and sell date

Counts retrospectively the best date for buying Bitcoin and then selling it.

`GET /bitcoin/api/v1/timemachine?from=2021-09-16&to=2021-12-20`

**Expected response**
```json
{
  "description": "Maximise profit by going back to these dates",
  "optimalBuyDate": "2021-09-22",
  "optimalSellDate": "2021-11-09"
}
```
or
```json
{
  "description": "No profit can be made during this time range"
}
```
