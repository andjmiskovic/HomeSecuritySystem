#!/bin/bash
docker stop mongodb
docker rm mongodb
echo "MongoDB container stopped and removed."