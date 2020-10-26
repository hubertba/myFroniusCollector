# myFroniusCollector

# data collector as springboot, so it can be trigger via a cronjob.

# database postgres

docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=root -d postgres 

# visualisation grafana
docker run -d --name=grafana -p 3000:3000 grafana/grafana