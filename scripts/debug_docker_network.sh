#!/bin/zsh

docker ps --filter "network=default" --format "{{.Names}}" | while read container; do
  echo "Inspecting $container..."
  docker inspect --format='{{.Name}}:
    Ports: {{json .NetworkSettings.Ports}}
    DNS: {{range .NetworkSettings.Networks}}{{.DNSNames}}{{end}}' "$container"
done;