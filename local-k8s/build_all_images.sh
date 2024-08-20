#!/bin/bash

# Create database image
cd database
docker build -t to-do-db .

cd ..

# Create backend image
cd backend
docker build -t to-do-bk .

cd ..

# Create gateway image
cd gateway
docker build -t to-do-gw .

cd ..

# Create frontend image
cd frontend
docker build -t to-do-fr .

cd ..